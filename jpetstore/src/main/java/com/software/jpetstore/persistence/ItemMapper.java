package com.software.jpetstore.persistence;

import java.util.List;
import java.util.Map;

import com.software.jpetstore.domain.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemMapper {
    void updateInventoryQuantity(Map<String, Object> param);
    int getInventoryQuantity(String itemId);
    List<Item> getItemListByProduct(String productId);
    Item getItem(String itemId);
}
