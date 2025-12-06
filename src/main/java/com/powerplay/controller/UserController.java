package com.powerplay.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.powerplay.dto.UserResponse;
import com.powerplay.dto.UserRoleRequest;
import com.powerplay.service.AuthService;
import com.powerplay.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

  private final UserService userService;
  private final AuthService authService;

  public UserController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
  }

  @GetMapping
  public List<UserResponse> list(@RequestHeader("Authorization") String authHeader) {
    authService.requireAdmin(authHeader);
    return userService.getUsers();
  }

  @PutMapping("/{id}/role")
  public ResponseEntity<UserResponse> updateRole(@PathVariable String id,
                                                 @RequestHeader("Authorization") String authHeader,
                                                 @Valid @RequestBody UserRoleRequest request) {
    authService.requireAdmin(authHeader);
    return ResponseEntity.ok(userService.updateRole(id, request.getRole()));
  }
}
