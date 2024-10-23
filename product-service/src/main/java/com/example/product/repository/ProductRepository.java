package com.example.product.repository;

import com.couchbase.client.core.deps.com.google.gson.Gson;
import com.couchbase.client.core.deps.com.google.gson.GsonBuilder;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.json.*;
import com.couchbase.client.java.transactions.TransactionGetResult;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.kv.*;
import com.couchbase.client.java.manager.bucket.BucketManager;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.example.product.configuration.couchbase.CouchbaseConfiguration;
import com.example.product.dto.ProductRequest;
import com.example.product.dto.UpdateStockRequest;
import com.example.product.exception.StockUpdateException;
import com.example.product.model.Product;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final Cluster cluster;
    private final Collection collection;
    private final CouchbaseConfiguration couchbaseConfig;
    private final Gson gson;
    private final MessageSource messageSource;


    public ProductRepository(Cluster cluster, Bucket bucket, CouchbaseConfiguration couchbaseConfig, MessageSource messageSource) {
        this.cluster = cluster;
        this.collection = bucket.defaultCollection();
        this.couchbaseConfig = couchbaseConfig;
        this.messageSource = messageSource;
        this.gson = new GsonBuilder().create();
    }


    public void findByDestinationAirport() {
        MutateInResult mutateInResult = this.collection.mutateIn("productId", List.of(
                MutateInSpec.increment("stockQuantity", 2)
        ));

    }

    public List<Product> findAllById(List<String> productIds) {
        List<Product> products = new ArrayList<>();
        productIds.parallelStream().forEach(id -> {
            try {
                GetResult result = collection.get(id);
                Product product = gson.fromJson(result.contentAsObject().toString(), Product.class);
                product.setId(id);
                products.add(product);
                System.out.println("Doküman ID: " + id + " İçerik: " + product.getName());

            } catch (Exception e) {
                System.err.println("Doküman ID: " + id + " getirilemedi: " + e.getMessage());
            }
        });
        return products;
    }

    public Optional<Product> findById(String id) {
        try {
            GetResult getResult = collection.get(id);
            Product product = gson.fromJson(getResult.contentAsObject().toString(), Product.class);
            return Optional.ofNullable(product);
        } catch (DocumentNotFoundException e) {
            System.err.println("Doküman bulunamadı: " + id);
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Bir hata oluştu: " + e.getMessage());
            return Optional.empty();
        }
    }


    public void createBuckets(List<String> bucketNames) {
        BucketManager bucketManager = cluster.buckets();

        for (String bucketName : bucketNames) {
            if (bucketManager.getAllBuckets().containsKey(bucketName)) {
                System.out.println(bucketName + " bucket'ı zaten mevcut.");
            } else {
                BucketSettings bucketSettings = BucketSettings.create(bucketName)
                        .ramQuotaMB(100);

                bucketManager.createBucket(bucketSettings);
                System.out.println(bucketName + " bucket'ı başarıyla oluşturuldu.");
            }
        }
    }

    public void bulkInsertAsync(List<Product> productList) {
        ReactiveCollection reactiveCollection = collection.reactive();
        Flux<MutationResult> resultFlux = Flux.fromIterable(productList)
                .flatMap(product -> {
                    JsonObject content = JsonObject.fromJson(gson.toJson(product));
                    return reactiveCollection.upsert(product.getId(), content);
                });

        resultFlux.subscribe(
                result -> System.out.println("Başarıyla eklendi: " + result.toString()),
                error -> System.err.println("Hata: " + error.getMessage()),
                () -> System.out.println("Bulk insert işlemi tamamlandı.")
        );
    }

    public void decrementStocks(UpdateStockRequest updateStockRequest) {
        try {
            this.cluster.transactions().run(ctx -> {
                for (ProductRequest productRequest : updateStockRequest.getProducts()) {
                    String productId = productRequest.getId();
                    int quantityToDecrement = productRequest.getQuantity();

                    TransactionGetResult product = ctx.get(this.collection, productId);
                    JsonObject productContent = product.contentAs(JsonObject.class);

                    int currentStock = productContent.getInt("stockQuantity");
                    if (!productContent.containsKey("isValid") || !productContent.getBoolean("isValid")) {
                        String message = messageSource.getMessage("error.stockUpdate.invalidProduct", null, "Unknown error", Locale.getDefault());
                        throw new StockUpdateException(message, null);
                    }

                    if (currentStock < quantityToDecrement) {
                        String message = messageSource.getMessage("error.stockUpdate.insufficientStock", null, "Unknown error", Locale.getDefault());
                        throw new StockUpdateException(message, null);
                    }
                    int updatedStock = currentStock - quantityToDecrement;
                    productContent.put("stockQuantity", updatedStock);

                    ctx.replace(product, productContent);
                }
            });
        } catch (Exception e) {
            String message = messageSource.getMessage("error.stockUpdate.failed", null, "Unknown error", Locale.getDefault());
            System.out.println("An error occurred while decrementing stock: " + e.getMessage() + e);
            throw new StockUpdateException(message, e);
        }
    }


//    @Retryable(
//            value = {StockCasMisMatchException.class},
//            maxAttempts = 2,
//            backoff = @Backoff(delay = 2000)
//    )
//    public void decrementStock(String productId, int quantityToDecrement) {
//        try {
//            GetResult result = this.collection.get(productId);
//            long cas = result.cas();
//            Product product = gson.fromJson(result.contentAsObject().toString(), Product.class);
//
//            int currentStock = product.getStockQuantity();
//
//            if (!product.isValid()) {
//                String message = messageSource.getMessage("error.stockUpdate.invalidProduct", null, "Unknown error", Locale.getDefault());
//                throw new StockUpdateException(message, null);
//            }
//            if (currentStock < quantityToDecrement) {
//                String message = messageSource.getMessage("error.stockUpdate.insufficientStock", null, "Unknown error", Locale.getDefault());
//                throw new StockUpdateException(message, null);
//            }
//            MutationResult mutationResult = this.collection.mutateIn(productId,
//                    List.of(
//                            MutateInSpec.decrement("stockQuantity", quantityToDecrement)
//                    ),
//                    MutateInOptions.mutateInOptions()
//                            .durability(DurabilityLevel.NONE)
//                            .cas(cas)
//            );
//
//            System.out.println("Stock successfully decremented.");
//        } catch (CasMismatchException e) {
//            System.out.println("CAS mismatch occurred, will retry...");
//            String message = messageSource.getMessage("error.stockUpdate.failed", null, "Unknown error", Locale.getDefault());
//            throw new StockCasMisMatchException(message, e);
//        } catch (Exception e) {
//            String message = messageSource.getMessage("error.stockUpdate.failed", null, "Unknown error", Locale.getDefault());
//            System.out.println("An error occurred while decrementing stock: " + e.getMessage() + e);
//
//            throw new StockUpdateException(message, e);
//        }
//    }
}


