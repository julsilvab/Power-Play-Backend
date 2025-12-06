package com.powerplay.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.powerplay.dto.CategoryRequest;
import com.powerplay.dto.CategoryResponse;
import com.powerplay.service.AuthService;
import com.powerplay.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

  private final CategoryService categoryService;
  private final AuthService authService;

  public CategoryController(CategoryService categoryService, AuthService authService) {
    this.categoryService = categoryService;
    this.authService = authService;
  }

  @GetMapping
  public List<CategoryResponse> list() {
    return categoryService.getCategories();
  }

  @PostMapping
  public ResponseEntity<CategoryResponse> create(@RequestHeader("Authorization") String authHeader,
                                                 @Valid @RequestBody CategoryRequest request) {
    authService.requireAdmin(authHeader);
    return ResponseEntity.ok(categoryService.create(request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@RequestHeader("Authorization") String authHeader,
                                     @PathVariable String id) {
    authService.requireAdmin(authHeader);
    categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
