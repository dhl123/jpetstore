package com.software.jpetstore.persistence;

import java.util.List;

import com.software.jpetstore.domain.Order;
import com.software.jpetstore.domain.Order;

public interface OrderMapper {

  List<Order> getOrdersByUsername(String username);

  Order getOrder(int orderId);
  
  void insertOrder(Order order);
  
  void insertOrderStatus(Order order);

}
