package com.example.order_service.repository;

import com.example.order_service.model.Order;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CouchbaseRepository<Order,String> {
}
