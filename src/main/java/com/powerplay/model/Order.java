package com.powerplay.model;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "orders")
public class Order {

  @Id
  private String id;

  @Field("user_id")
  private String userId;

  private String status;

  @Field("cart_items")
  private List<OrderItem> cartItems;

  @Field("form_data")
  private OrderForm formData;

  private long subtotal;

  private long shipping;

  private long total;

  @CreatedDate
  @Field("created_at")
  private Instant createdAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<OrderItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(List<OrderItem> cartItems) {
    this.cartItems = cartItems;
  }

  public OrderForm getFormData() {
    return formData;
  }

  public void setFormData(OrderForm formData) {
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public static class OrderItem {
    private String id;
    private String productId;
    private String name;
    private long price;
    private int quantity;
    private String image;
    private String sku;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getProductId() {
      return productId;
    }

    public void setProductId(String productId) {
      this.productId = productId;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public long getPrice() {
      return price;
    }

    public void setPrice(long price) {
      this.price = price;
    }

    public int getQuantity() {
      return quantity;
    }

    public void setQuantity(int quantity) {
      this.quantity = quantity;
    }

    public String getImage() {
      return image;
    }

    public void setImage(String image) {
      this.image = image;
    }

    public String getSku() {
      return sku;
    }

    public void setSku(String sku) {
      this.sku = sku;
    }
  }

  public static class OrderForm {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private Boolean transferenceConfirmed;
    private String transactionId;

    public String getFullName() {
      return fullName;
    }

    public void setFullName(String fullName) {
      this.fullName = fullName;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public String getPhone() {
      return phone;
    }

    public void setPhone(String phone) {
      this.phone = phone;
    }

    public String getAddress() {
      return address;
    }

    public void setAddress(String address) {
      this.address = address;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getPostalCode() {
      return postalCode;
    }

    public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
    }

    public Boolean getTransferenceConfirmed() {
      return transferenceConfirmed;
    }

    public void setTransferenceConfirmed(Boolean transferenceConfirmed) {
      this.transferenceConfirmed = transferenceConfirmed;
    }

    public String getTransactionId() {
      return transactionId;
    }

    public void setTransactionId(String transactionId) {
      this.transactionId = transactionId;
    }
  }
}
