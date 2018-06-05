package com.software.jpetstore.persistence;

import java.util.List;

import com.software.jpetstore.domain.LineItem;
import com.software.jpetstore.domain.LineItem;

public interface LineItemMapper {

  List<LineItem> getLineItemsByOrderId(int orderId);

  void insertLineItem(LineItem lineItem);

}
