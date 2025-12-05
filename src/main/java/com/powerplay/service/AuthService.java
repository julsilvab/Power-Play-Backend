package com.powerplay.service;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.powerplay.dto.AuthRequest;
import com.powerplay.dto.AuthResponse;
import com.powerplay.dto.SignupRequest;
import com.powerplay.dto.UserResponse;
import com.powerplay.exception.ApiException;
import com.powerplay.model.User;
import com.powerplay.repository.UserRepository;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public AuthResponse signup(SignupRequest request) {
    Optional<User> existing = userRepository.findByEmail(request.getEmail());
    if (existing.isPresent()) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
    }

    User user = new User();
    user.setName(request.getName());
    user.setEmail(request.getEmail().toLowerCase());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole("user");
    userRepository.save(user);

    return new AuthResponse(mapUser(user), jwtService.generateToken(user));
  }

  public AuthResponse login(AuthRequest request) {
    User user = userRepository.findByEmail(request.getEmail().toLowerCase())
      .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Credenciales inválidas"));

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Credenciales inválidas");
    }

    return new AuthResponse(mapUser(user), jwtService.generateToken(user));
  }

  public User requireUser(String authHeader) {
    String token = extractToken(authHeader);
    DecodedJWT decoded = jwtService.verify(token);
    String userId = decoded.getSubject();
    return userRepository.findById(userId)
      .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
  }

  public User requireAdmin(String authHeader) {
    User user = requireUser(authHeader);
    if (!"admin".equalsIgnoreCase(user.getRole())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Solo los administradores pueden realizar esta acción");
    }
    return user;
  }

  public UserResponse mapUser(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
  }

  private String extractToken(String authHeader) {
    if (authHeader == null || authHeader.isBlank()) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Falta el encabezado Authorization");
    }

    if (authHeader.startsWith("Bearer ")) {
      return authHeader.substring(7);
    }

    throw new ApiException(HttpStatus.UNAUTHORIZED, "Formato de token inválido");
  }
}
