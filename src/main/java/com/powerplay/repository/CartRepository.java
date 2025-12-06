package com.powerplay.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.powerplay.model.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {
  Optional<Cart> findByUserId(String userId);
}
