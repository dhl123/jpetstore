package com.software.jpetstore.service;

import com.software.jpetstore.domain.Order;
import com.software.jpetstore.domain.Cart;
import com.software.jpetstore.domain.Order;

import java.util.List;

public interface OrderService {

    void insertOrder(Order order);//插入新订单
    Order getOrder(int orderId);//根据订单 ID 查找订单
    List<Order> getOrdersByUsername(String username);//根据用户名查找订单
}
