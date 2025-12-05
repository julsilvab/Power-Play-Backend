package com.powerplay.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

  @Value("${app.client-url:http://localhost:5173}")
  private String clientUrl;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    String[] origins = Arrays.stream(clientUrl.split(","))
      .map(String::trim)
      .filter(s -> !s.isBlank())
      .toArray(String[]::new);

    if (origins.length == 0) {
      origins = new String[] {"*"};
    }

    registry.addMapping("/**")
      .allowedOriginPatterns(origins)
      .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
      .allowedHeaders("*")
      .allowCredentials(true)
      .maxAge(3600);
  }
}
