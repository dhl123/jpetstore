package com.software.jpetstore.persistence;

import java.util.List;

import com.software.jpetstore.domain.LineItem;
import com.software.jpetstore.domain.LineItem;
import org.springframework.stereotype.Repository;

@Repository
public interface LineItemMapper {
    List<LineItem> getLineItemsByOrderId(int orderId);
    void insertLineItem(LineItem lineItem);
}
