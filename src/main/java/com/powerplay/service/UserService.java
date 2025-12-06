package com.powerplay.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.powerplay.dto.UserResponse;
import com.powerplay.exception.ApiException;
import com.powerplay.model.User;
import com.powerplay.repository.UserRepository;

@Service
public class UserService {

  private static final Set<String> ALLOWED_ROLES = Set.of("user", "admin");

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void deleteUser(String userId) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    if ("admin".equalsIgnoreCase(user.getRole())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "No puedes eliminar un administrador");
    }

    userRepository.delete(user);
  }

  public List<UserResponse> getUsers() {
    return userRepository.findAll().stream()
      .map(this::mapUser)
      .collect(Collectors.toList());
  }

  public UserResponse updateRole(String userId, String role) {
    String normalizedRole = role == null ? "" : role.toLowerCase();
    if (!ALLOWED_ROLES.contains(normalizedRole)) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Rol inválido");
    }

    User user = userRepository.findById(userId)
      .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

    user.setRole(normalizedRole);
    return mapUser(userRepository.save(user));
  }

  private UserResponse mapUser(User user) {
    return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
  }
}
