package com.powerplay.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.powerplay.model.Product;
import com.powerplay.model.User;
import com.powerplay.repository.ProductRepository;
import com.powerplay.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(DataSeeder.class);

  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final PasswordEncoder passwordEncoder;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Value("${app.admin.email:admin@powerplay.com}")
  private String adminEmail;

  @Value("${app.admin.password:admin123}")
  private String adminPassword;

  public DataSeeder(UserRepository userRepository,
                    ProductRepository productRepository,
                    PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.productRepository = productRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) throws Exception {
    seedAdminUser();
    seedProducts();
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

  private void seedProducts() {
    if (productRepository.count() > 0) {
      return;
    }

    try (InputStream input = new ClassPathResource("products.json").getInputStream()) {
      ProductSeed[] products = objectMapper.readValue(input, ProductSeed[].class);
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
    } catch (IOException e) {
      LOGGER.error("No se pudieron cargar los productos iniciales", e);
    }
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
