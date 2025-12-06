package com.powerplay.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.powerplay.model.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
  Optional<Category> findBySlug(String slug);
}
