package com.powerplay.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class CartUpdateRequest {

  @Valid
  @NotNull
  private List<CartItemRequest> items;

  public List<CartItemRequest> getItems() {
    return items;
  }

  public void setItems(List<CartItemRequest> items) {
    this.items = items;
  }
}
