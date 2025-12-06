package com.powerplay.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerplay.model.Category;
import com.powerplay.model.Product;
import com.powerplay.model.User;
import com.powerplay.repository.CategoryRepository;
import com.powerplay.repository.ProductRepository;
import com.powerplay.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataSeeder.class);

  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final PasswordEncoder passwordEncoder;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${app.admin.email:admin@powerplay.com}")
  private String adminEmail;

  @Value("${app.admin.password:admin123}")
  private String adminPassword;

  public DataSeeder(UserRepository userRepository,
                    ProductRepository productRepository,
                    CategoryRepository categoryRepository,
                    PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.categoryRepository = categoryRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    seedAdminUser();
    ProductSeed[] products = loadProductSeeds();
    seedProducts(products);
    seedCategories(products);
  }

  private void seedAdminUser() {
    if (userRepository.findByEmail(adminEmail.toLowerCase()).isPresent()) {
      return;
    }
    User admin = new User();
    admin.setName("Administrador");
    admin.setEmail(adminEmail.toLowerCase());
    admin.setPassword(passwordEncoder.encode(adminPassword));
    admin.setRole("admin");
    userRepository.save(admin);
    LOGGER.info("Usuario admin creado con email {}", adminEmail);
  }

  private ProductSeed[] loadProductSeeds() {
    try (InputStream input = new ClassPathResource("products.json").getInputStream()) {
      return objectMapper.readValue(input, ProductSeed[].class);
    } catch (IOException e) {
      LOGGER.error("No se pudieron leer los productos iniciales", e);
      return new ProductSeed[0];
    }
  }

  private void seedProducts(ProductSeed[] products) {
    if (productRepository.count() > 0 || products.length == 0) {
      return;
    }

    Arrays.stream(products).forEach(seed -> {
      Product product = new Product();
      product.setId(seed.id);
      product.setName(seed.name);
      product.setPrice(seed.price);
      product.setCategory(seed.category);
      product.setImage(seed.image);
      product.setSku(seed.sku);
      product.setDescription(seed.description);
      product.setFeatured(seed.featured);
      product.setLegacyId(seed.id);
      productRepository.save(product);
    });
    LOGGER.info("Se cargaron {} productos iniciales", products.length);
  }

  private void seedCategories(ProductSeed[] products) {
    if (products.length == 0) {
      return;
    }

    Map<String, String> uniqueCategories = Arrays.stream(products)
      .map(seed -> seed.category)
      .filter(cat -> cat != null && !cat.isBlank())
      .collect(Collectors.toMap(
        cat -> cat,
        this::formatCategoryName,
        (existing, replacement) -> existing,
        LinkedHashMap::new
      ));

    uniqueCategories.forEach((slug, name) -> {
      if (categoryRepository.findBySlug(slug).isEmpty()) {
        Category category = new Category();
        category.setId(slug);
        category.setName(name);
        category.setSlug(slug);
        category.setDescription(String.format("Productos de %s", name));
        categoryRepository.save(category);
      }
    });
    LOGGER.info("Se aseguraron {} categorías iniciales", uniqueCategories.size());
  }

  private String formatCategoryName(String slug) {
    return Arrays.stream(slug.split("[-_ ]"))
      .filter(part -> !part.isBlank())
      .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
      .collect(Collectors.joining(" "));
  }

  private static class ProductSeed {
    public String id;
    public String name;
    public long price;
    public String image;
    public String category;
    public String sku;
    public String description;
    public Boolean featured;
  }
}
