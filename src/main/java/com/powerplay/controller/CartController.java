package com.powerplay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.powerplay.dto.CartResponse;
import com.powerplay.dto.CartUpdateRequest;
import com.powerplay.model.User;
import com.powerplay.service.AuthService;
import com.powerplay.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
@Validated
public class CartController {

  private final CartService cartService;
  private final AuthService authService;

  public CartController(CartService cartService, AuthService authService) {
    this.cartService = cartService;
    this.authService = authService;
  }

  @GetMapping
  public CartResponse getCart(@RequestHeader("Authorization") String authHeader) {
    User user = authService.requireUser(authHeader);
    return cartService.getCart(user);
  }

  @PutMapping
  public CartResponse saveCart(@RequestHeader("Authorization") String authHeader,
                               @Valid @RequestBody CartUpdateRequest request) {
    User user = authService.requireUser(authHeader);
    return cartService.saveCart(user, request.getItems());
  }

  @DeleteMapping
  public ResponseEntity<Void> clearCart(@RequestHeader("Authorization") String authHeader) {
    User user = authService.requireUser(authHeader);
    cartService.clearCart(user);
    return ResponseEntity.noContent().build();
  }
}
