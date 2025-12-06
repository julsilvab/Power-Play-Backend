package com.powerplay.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.powerplay.dto.CategoryRequest;
import com.powerplay.dto.CategoryResponse;
import com.powerplay.exception.ApiException;
import com.powerplay.model.Category;
import com.powerplay.repository.CategoryRepository;

@Service
public class CategoryService {

  private final CategoryRepository categoryRepository;

  public CategoryService(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
  }

  public List<CategoryResponse> getCategories() {
    return categoryRepository.findAll().stream()
      .sorted(Comparator.comparing(Category::getName))
      .map(this::mapCategory)
      .collect(Collectors.toList());
  }

  public CategoryResponse create(CategoryRequest request) {
    categoryRepository.findBySlug(request.getSlug().toLowerCase())
      .ifPresent(existing -> {
        throw new ApiException(HttpStatus.BAD_REQUEST, "El slug ya existe");
      });

    Category category = new Category();
    category.setName(request.getName());
    category.setSlug(request.getSlug().toLowerCase());
    category.setDescription(request.getDescription());
    return mapCategory(categoryRepository.save(category));
  }

  public void delete(String id) {
    Category category = categoryRepository.findById(id)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Categoría no encontrada"));
    categoryRepository.delete(category);
  }

  private CategoryResponse mapCategory(Category category) {
    return new CategoryResponse(
      category.getId(),
      category.getName(),
      category.getSlug(),
      category.getDescription()
    );
  }
}
