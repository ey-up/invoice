package com.example.product.configuration.couchbase;

import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.query.QueryScanConsistency;
import com.couchbase.client.java.transactions.config.TransactionsCleanupConfig;
import com.couchbase.client.java.transactions.config.TransactionsConfig;
import com.couchbase.client.java.transactions.config.TransactionsQueryConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.couchbase.client.core.error.BucketNotFoundException;
import com.couchbase.client.core.error.UnambiguousTimeoutException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

import java.time.Duration;

@Configuration
@Getter
public class CouchbaseConfiguration {

    @Value("${spring.couchbase.host}")
    private String host;

    @Value("${spring.couchbase.bucket.user}")
    private String username;

    @Value("${spring.couchbase.bucket.password}")
    private String password;

    @Value("${spring.couchbase.bucket.name}")
    private String bucketName;

    @Value("${spring.couchbase.cluster.timeout}")
    private int clusterTimeout;

    @Value("${spring.couchbase.bucket.timeout}")
    private int bucketTimeout;


    @Bean(destroyMethod = "disconnect")
    Cluster getCouchbaseCluster() {
        try {
            System.out.println("Connecting to Couchbase cluster at " + host);

            ClusterEnvironment env = ClusterEnvironment.builder()
                    .transactionsConfig(TransactionsConfig.durabilityLevel(DurabilityLevel.NONE)
                            .timeout(Duration.ofSeconds(1111))
                            .cleanupConfig(TransactionsCleanupConfig.cleanupWindow(Duration.ofSeconds(30)))
                            .queryConfig(TransactionsQueryConfig.scanConsistency(QueryScanConsistency.NOT_BOUNDED)))
                    .build();
            Cluster cluster = Cluster.connect(host, ClusterOptions.clusterOptions(username, password)
                    .environment(env));
            cluster.waitUntilReady(Duration.ofSeconds(clusterTimeout));
            return cluster;
        } catch (UnambiguousTimeoutException e) {
            System.out.println("Connection to Couchbase cluster at " + host + " timed out");
            throw e;
        } catch (Exception e) {
            System.out.println(e.getClass().getName());
            System.out.println("Could not connect to Couchbase cluster at " + host);
            throw e;
        }

    }

    @Bean
    Bucket getCouchbaseBucket(Cluster cluster) {
        try {
            if (!cluster.buckets().getAllBuckets().containsKey(bucketName)) {
                throw new BucketNotFoundException("Bucket " + bucketName + " does not exist");
            }
            Bucket bucket = cluster.bucket(bucketName);
            bucket.waitUntilReady(Duration.ofSeconds(bucketTimeout));
            return bucket;
        } catch (UnambiguousTimeoutException e) {
            System.out.println("Connection to bucket " + bucketName + " timed out");
            throw e;
        } catch (BucketNotFoundException e) {
            System.out.println("Bucket " + bucketName + " does not exist");
            throw e;
        } catch (Exception e) {
            System.out.println(e.getClass().getName());
            System.out.println("Could not connect to bucket " + bucketName);
            throw e;
        }
    }

}
