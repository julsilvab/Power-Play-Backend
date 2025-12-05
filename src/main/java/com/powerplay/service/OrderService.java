package com.powerplay.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.powerplay.dto.OrderItemRequest;
import com.powerplay.dto.OrderRequest;
import com.powerplay.model.Order;
import com.powerplay.model.Order.OrderForm;
import com.powerplay.model.Order.OrderItem;
import com.powerplay.model.User;
import com.powerplay.repository.OrderRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public List<Order> getOrdersForUser(User user) {
    return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
  }

  public Order createOrder(User user, OrderRequest request) {
    Order order = new Order();
    order.setUserId(user.getId());
    order.setStatus("Pendiente");
    order.setCartItems(mapItems(request.getCartItems()));
    order.setFormData(mapForm(request));
    order.setSubtotal(request.getSubtotal());
    order.setShipping(request.getShipping());
    order.setTotal(request.getTotal());
    return orderRepository.save(order);
  }

  private List<OrderItem> mapItems(List<OrderItemRequest> items) {
    return items.stream().map(item -> {
      OrderItem orderItem = new OrderItem();
      orderItem.setId(item.getId());
      orderItem.setProductId(item.getProductId());
      orderItem.setName(item.getName());
      orderItem.setPrice(item.getPrice());
      orderItem.setQuantity(item.getQuantity());
      orderItem.setImage(item.getImage());
      orderItem.setSku(item.getSku());
      return orderItem;
    }).collect(Collectors.toList());
  }

  private OrderForm mapForm(OrderRequest request) {
    OrderForm form = new OrderForm();
    form.setFullName(request.getFormData().getFullName());
    form.setEmail(request.getFormData().getEmail());
    form.setPhone(request.getFormData().getPhone());
    form.setAddress(request.getFormData().getAddress());
    form.setCity(request.getFormData().getCity());
    form.setPostalCode(request.getFormData().getPostalCode());
    form.setTransferenceConfirmed(request.getFormData().getTransferenceConfirmed());
    form.setTransactionId(request.getFormData().getTransactionId());
    return form;
  }
}
