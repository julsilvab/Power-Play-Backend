package com.powerplay.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.powerplay.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
  Optional<Product> findByLegacyId(String legacyId);
}
