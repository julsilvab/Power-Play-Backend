package com.powerplay.dto;

public record CartItemResponse(
  String productId,
  String name,
  long price,
  String image,
  String sku,
  int quantity
) {}
