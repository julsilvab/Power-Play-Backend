package com.powerplay.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.powerplay.dto.OrderRequest;
import com.powerplay.dto.OrderStatusRequest;
import com.powerplay.model.Order;
import com.powerplay.model.User;
import com.powerplay.service.AuthService;
import com.powerplay.service.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

  private final OrderService orderService;
  private final AuthService authService;

  public OrderController(OrderService orderService, AuthService authService) {
    this.orderService = orderService;
    this.authService = authService;
  }

  @GetMapping
  public List<Order> list(@RequestHeader("Authorization") String authHeader) {
    User user = authService.requireUser(authHeader);
    return orderService.getOrdersForUser(user);
  }

  @GetMapping("/admin")
  public List<Order> listAll(@RequestHeader("Authorization") String authHeader) {
    authService.requireAdmin(authHeader);
    return orderService.getAllOrders();
  }

  @PostMapping
  public ResponseEntity<Order> create(@RequestHeader("Authorization") String authHeader,
                                      @Valid @RequestBody OrderRequest request) {
    User user = authService.requireUser(authHeader);
    return ResponseEntity.ok(orderService.createOrder(user, request));
  }

  @PutMapping("/{orderId}/status")
  public ResponseEntity<Order> updateStatus(@PathVariable String orderId,
                                            @RequestHeader("Authorization") String authHeader,
                                            @Valid @RequestBody OrderStatusRequest request) {
    authService.requireAdmin(authHeader);
    return ResponseEntity.ok(orderService.updateStatus(orderId, request.getStatus()));
  }
}
