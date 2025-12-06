package com.powerplay.repository;

<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> b47a662 (Add categories and cart persistence)
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.powerplay.model.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {
  Optional<Cart> findByUserId(String userId);
  List<Cart> findAllByUserId(String userId);
}
