package com.powerplay.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.powerplay.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
  List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

  List<Order> findAllByOrderByCreatedAtDesc();
}
