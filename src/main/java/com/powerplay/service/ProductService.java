package com.powerplay.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.powerplay.dto.ProductRequest;
import com.powerplay.dto.ProductResponse;
import com.powerplay.exception.ApiException;
import com.powerplay.model.Product;
import com.powerplay.repository.ProductRepository;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductResponse> getAll() {
    return productRepository.findAll().stream()
      .sorted(Comparator.comparing(Product::getName))
      .map(this::mapProduct)
      .collect(Collectors.toList());
  }

  public ProductResponse create(ProductRequest request) {
    Product product = new Product();
    applyRequest(product, request);
    return mapProduct(productRepository.save(product));
  }

  public ProductResponse update(String id, ProductRequest request) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    applyRequest(product, request);
    return mapProduct(productRepository.save(product));
  }

  public void delete(String id) {
    Product product = productRepository.findById(id)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    productRepository.delete(product);
  }

  private void applyRequest(Product product, ProductRequest request) {
    product.setName(request.getName());
    product.setPrice(request.getPrice());
    product.setCategory(request.getCategory());
    product.setImage(request.getImage());
    product.setSku(request.getSku());
    product.setDescription(request.getDescription());
    product.setFeatured(request.getFeatured());
  }

  private ProductResponse mapProduct(Product product) {
    return new ProductResponse(
      product.getId(),
      product.getName(),
      product.getPrice(),
      product.getCategory(),
      product.getImage(),
      product.getSku(),
      product.getDescription(),
      product.getFeatured()
    );
  }
}
