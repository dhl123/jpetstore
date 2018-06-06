package com.software.jpetstore.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.software.jpetstore.domain.Item;
import com.software.jpetstore.domain.LineItem;
import com.software.jpetstore.domain.Order;
import com.software.jpetstore.domain.Sequence;
import com.software.jpetstore.persistence.ItemMapper;
import com.software.jpetstore.persistence.LineItemMapper;
import com.software.jpetstore.persistence.OrderMapper;
import com.software.jpetstore.persistence.SequenceMapper;
import com.software.jpetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {
    private final ItemMapper itemMapper;
    private final OrderMapper orderMapper;
    private final SequenceMapper sequenceMapper;
    private final LineItemMapper lineItemMapper;

    @Autowired
    public OrderServiceImpl(ItemMapper itemMapper, OrderMapper orderMapper, SequenceMapper sequenceMapper, LineItemMapper lineItemMapper) {
      this.itemMapper = itemMapper;
      this.orderMapper = orderMapper;
      this.sequenceMapper = sequenceMapper;
      this.lineItemMapper = lineItemMapper;
    }

    @Transactional
    public void insertOrder(Order order) {
      order.setOrderId(getNextId("ordernum"));
      for (int i = 0; i < order.getLineItems().size(); i++) {
        LineItem lineItem = order.getLineItems().get(i);
        String itemId = lineItem.getItemId();
        Integer increment = new Integer(lineItem.getQuantity());
        Map<String, Object> param = new HashMap<>(2);
        param.put("itemId", itemId);
        param.put("increment", increment);
        itemMapper.updateInventoryQuantity(param);
      }

      orderMapper.insertOrder(order);
      orderMapper.insertOrderStatus(order);
      for (int i = 0; i < order.getLineItems().size(); i++) {
        LineItem lineItem = order.getLineItems().get(i);
        lineItem.setOrderId(order.getOrderId());
        lineItemMapper.insertLineItem(lineItem);
      }
    }

    @Transactional
    public Order getOrder(int orderId) {
      Order order = orderMapper.getOrder(orderId);
      order.setLineItems(lineItemMapper.getLineItemsByOrderId(orderId));

      for (int i = 0; i < order.getLineItems().size(); i++) {
        LineItem lineItem = order.getLineItems().get(i);
        Item item = itemMapper.getItem(lineItem.getItemId());
        item.setQuantity(itemMapper.getInventoryQuantity(lineItem.getItemId()));
        lineItem.setItem(item);
      }

      return order;
    }

    public List<Order> getOrdersByUsername(String username) {
      return orderMapper.getOrdersByUsername(username);
    }

    public int getNextId(String name) {
      Sequence sequence = new Sequence(name, -1);
      sequence = sequenceMapper.getSequence(sequence);
      if (sequence == null) {
        throw new RuntimeException("Error: A null sequence was returned from the database (could not get next " + name
            + " sequence).");
      }
      Sequence parameterObject = new Sequence(name, sequence.getNextId() + 1);
      sequenceMapper.updateSequence(parameterObject);
      return sequence.getNextId();
    }
}
