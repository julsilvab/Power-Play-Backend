package com.powerplay.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.powerplay.exception.ApiException;
import com.powerplay.model.User;

import org.springframework.http.HttpStatus;

@Service
public class JwtService {

  @Value("${app.jwt.secret:}")
  private String jwtSecret;

  private Algorithm algorithm() {
    if (jwtSecret == null || jwtSecret.isBlank()) {
      throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "JWT_SECRET no está configurado");
    }
    return Algorithm.HMAC256(jwtSecret);
  }

  public String generateToken(User user) {
    return JWT.create()
      .withSubject(user.getId())
      .withClaim("role", user.getRole())
      .withIssuedAt(Instant.now())
      .withExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS))
      .sign(algorithm());
  }

  public DecodedJWT verify(String token) {
    try {
      return JWT.require(algorithm()).build().verify(token);
    } catch (JWTVerificationException ex) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Token inválido o expirado");
    }
  }
}
