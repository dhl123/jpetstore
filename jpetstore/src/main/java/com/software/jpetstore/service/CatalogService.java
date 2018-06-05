package com.software.jpetstore.service;

import com.software.jpetstore.domain.Category;
import com.software.jpetstore.domain.Item;
import com.software.jpetstore.domain.Product;

import java.util.List;

public interface CatalogService {

    List<Category> getCategoryList();    // 获得分类列表
    Category getCategory(String categoryId);   // 根据 ID 查找分类
    Product getProduct(String productId);    // 根据 ID 查找商品
    List<Product> getProductListByCategory(String categoryId);// 根据分类查找商品列表
    List<Product> searchProductList(String keyword);           // 根据关键词搜索商品
    List<Item> getItemListByProduct(String productId);      // 根据商品 ID 查找物品
    Item getItem(String itemId);                                // 根据 物品ID 查找物品
    boolean isItemInStock(String itemId);                              // 物品是否有货

}
