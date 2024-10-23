//package com.example.product.repository;
//
//import com.couchbase.client.core.deps.com.google.gson.Gson;
//import com.couchbase.client.core.deps.com.google.gson.GsonBuilder;
//import com.couchbase.client.core.error.DocumentNotFoundException;
//import com.couchbase.client.java.Bucket;
//import com.couchbase.client.java.Cluster;
//import com.couchbase.client.java.Collection;
//import com.couchbase.client.java.json.JsonObject;
//import com.couchbase.client.java.kv.GetResult;
//
//import com.example.product.configuration.couchbase.CouchbaseConfiguration;
//import com.example.product.model.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.context.MessageSource;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//public class ProductRepositoryTest {
//
//    private ProductRepository productRepository; // Test edilecek sınıf
//    private Collection collection; // Couchbase Collection mock nesnesi
//    private Gson gson; // Gson mock nesnesi
//
//    @BeforeEach
//    public void setUp() {
//        // Mock nesnelerin oluşturulması
//        collection = Mockito.mock(Collection.class);
//        MessageSource mock = mock(MessageSource.class);
//        CouchbaseConfiguration couchbaseConfig = Mockito.mock(CouchbaseConfiguration.class);
//        gson = new GsonBuilder().create();
//
//        // Test edilecek sınıfın oluşturulması
//        productRepository = new ProductRepository(Mockito.mock(Cluster.class),mock, Mockito.mock(Bucket.class), couchbaseConfig);
//
//        // Reflection kullanarak collection ve gson ayarlarını yapma
//        setPrivateField(productRepository, "collection", collection);
//        setPrivateField(productRepository, "gson", gson);
//    }
//
//    private void setPrivateField(Object object, String fieldName, Object value) {
//        try {
//            var field = object.getClass().getDeclaredField(fieldName);
//            field.setAccessible(true); // Private alanlara erişim izni
//            field.set(object, value);
//        } catch (NoSuchFieldException | IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testFindById_DocumentFound() throws Exception {
//        // given
//        String productId = "product_123";
//        Product expectedProduct = new Product();
//        expectedProduct.setId(productId);
//        expectedProduct.setName("Test Product");
//        expectedProduct.setStockQuantity(10);
//        expectedProduct.setValid(true);
//
//
//        GetResult mockGetResult = mock(GetResult.class);
//        when(collection.get(productId)).thenReturn(mockGetResult);
//        when(mockGetResult.contentAsObject()).thenReturn(JsonObject.fromJson(gson.toJson(expectedProduct)));
//
//        // Metodun çağrılması
//        Optional<Product> result = productRepository.findById(productId);
//
//        // Sonuç kontrolü
//        assertTrue(result.isPresent());
//        assertEquals(expectedProduct.getId(), result.get().getId());
//        assertEquals(expectedProduct.getName(), result.get().getName());
//    }
//
//    @Test
//    public void testFindById_DocumentNotFound() {
//        //given
//        String productId = "non_existent_product";
//        when(collection.get(productId)).thenThrow(DocumentNotFoundException.class);
//
//        // when
//        Optional<Product> result = productRepository.findById(productId);
//
//        // then
//        assertTrue(result.isEmpty());
//    }
//
//    @Test
//    public void testFindById_OtherException() {
//        // given
//        String productId = "product_123";
//
//        when(collection.get(productId)).thenThrow(new RuntimeException("Bir hata oluştu"));
//
//        // when
//        Optional<Product> result = productRepository.findById(productId);
//
//        // then
//        assertTrue(result.isEmpty());
//    }
//}