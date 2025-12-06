package com.powerplay.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.powerplay.dto.ProductRequest;
import com.powerplay.dto.ProductResponse;
import com.powerplay.service.AuthService;
import com.powerplay.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

  private final ProductService productService;
  private final AuthService authService;

  public ProductController(ProductService productService, AuthService authService) {
    this.productService = productService;
    this.authService = authService;
  }

  @GetMapping
  public List<ProductResponse> list() {
    return productService.getAll();
  }

  @PostMapping
  public ResponseEntity<ProductResponse> create(@RequestHeader("Authorization") String authHeader,
                                                @Valid @RequestBody ProductRequest request) {
    authService.requireAdmin(authHeader);
    return ResponseEntity.ok(productService.create(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductResponse> update(@PathVariable String id,
                                                 @RequestHeader("Authorization") String authHeader,
                                                 @Valid @RequestBody ProductRequest request) {
    authService.requireAdmin(authHeader);
    return ResponseEntity.ok(productService.update(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ProductResponse> delete(@PathVariable String id,
                                                @RequestHeader("Authorization") String authHeader) {
    authService.requireAdmin(authHeader);
    return ResponseEntity.ok(productService.delete(id));
  }
}
