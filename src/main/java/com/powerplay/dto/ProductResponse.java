package com.powerplay.dto;

public record ProductResponse(
  String id,
  String name,
  long price,
  String category,
  String image,
  String sku,
  String description,
  Boolean featured
) {}
