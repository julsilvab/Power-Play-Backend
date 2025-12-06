package com.powerplay.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.powerplay.dto.CartItemRequest;
import com.powerplay.dto.CartItemResponse;
import com.powerplay.dto.CartResponse;
import com.powerplay.model.Cart;
import com.powerplay.model.Cart.CartItem;
import com.powerplay.model.User;
import com.powerplay.repository.CartRepository;

@Service
public class CartService {

  private final CartRepository cartRepository;

  public CartService(CartRepository cartRepository) {
    this.cartRepository = cartRepository;
  }

  public CartResponse getCart(User user) {
    Cart cart = resolveCart(user);
    return mapCart(cart);
  }

  public CartResponse saveCart(User user, List<CartItemRequest> items) {
    Cart cart = resolveCart(user);

    List<CartItem> updatedItems = (items == null ? List.<CartItemRequest>of() : items).stream()
      .map(this::mapRequestToItem)
      .collect(Collectors.toList());

    cart.setItems(updatedItems);
    cart.setUpdatedAt(Instant.now());
    return mapCart(cartRepository.save(cart));
  }

  public void clearCart(User user) {
    Cart cart = resolveCart(user);
    cart.setItems(List.of());
    cart.setUpdatedAt(Instant.now());
    cartRepository.save(cart);
  }

  private Cart resolveCart(User user) {
    List<Cart> carts = cartRepository.findAllByUserId(user.getId());

    if (carts.isEmpty()) {
      return createEmptyCart(user);
    }

    Cart primary = carts.get(0);
    if (carts.size() > 1) {
      carts.stream().skip(1).forEach(cartRepository::delete);
    }

    return primary;
  }

  private Cart createEmptyCart(User user) {
    Cart cart = new Cart();
    cart.setUserId(user.getId());
    cart.setItems(List.of());
    cart.setUpdatedAt(Instant.now());
    return cartRepository.save(cart);
  }

  private CartItem mapRequestToItem(CartItemRequest request) {
    CartItem item = new CartItem();
    item.setProductId(request.getProductId());
    item.setName(request.getName());
    item.setPrice(request.getPrice());
    item.setImage(request.getImage());
    item.setSku(request.getSku());
    item.setQuantity(request.getQuantity());
    return item;
  }

  private CartResponse mapCart(Cart cart) {
    List<CartItemResponse> items = cart.getItems().stream()
      .map(item -> new CartItemResponse(
        item.getProductId(),
        item.getName(),
        item.getPrice(),
        item.getImage(),
        item.getSku(),
        item.getQuantity()
      ))
      .collect(Collectors.toList());

    return new CartResponse(cart.getUserId(), items, cart.getUpdatedAt());
  }
}
