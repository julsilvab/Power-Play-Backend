package com.powerplay.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

  @NotEmpty
  @Valid
  private List<OrderItemRequest> cartItems;

  @NotNull
  @Valid
  private OrderFormRequest formData;

  private long subtotal;

  private long shipping;

  private long total;

  public List<OrderItemRequest> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<OrderItemRequest> cartItems) {
    this.cartItems = cartItems;
  }

  public OrderFormRequest getFormData() {
    return formData;
  }

  public void setFormData(OrderFormRequest formData) {
    this.formData = formData;
  }

  public long getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(long subtotal) {
    this.subtotal = subtotal;
  }

  public long getShipping() {
    return shipping;
  }

  public void setShipping(long shipping) {
    this.shipping = shipping;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }
}
