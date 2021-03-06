package com.software.jpetstore.service.impl;

import com.software.jpetstore.domain.Category;
import com.software.jpetstore.domain.Item;
import com.software.jpetstore.domain.Product;
import com.software.jpetstore.persistence.CategoryMapper;
import com.software.jpetstore.persistence.ItemMapper;
import com.software.jpetstore.persistence.ProductMapper;
import com.software.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
@Service
public class CatalogServiceImpl implements CatalogService {
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    private final ItemMapper itemMapper;

    @Autowired
    public CatalogServiceImpl(CategoryMapper categoryMapper, ProductMapper productMapper, ItemMapper itemMapper) {
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.itemMapper = itemMapper;
    }

    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    public Category getCategory(String categoryId){
    return categoryMapper.getCategory(categoryId);
}

    public Product getProduct(String productId){
        return productMapper.getProduct(productId);
    }

    public List<Product> getProductListByCategory(String categoryId){
    return productMapper.getProductListByCategory(categoryId);
    }

    public List<Product> searchProductList(String keyword){
        return productMapper.searchProductList("%"+keyword+"%");
    }

    public List<Item> getItemListByProduct(String productId){
        return itemMapper.getItemListByProduct(productId);
    }
    public Item getItem(String itemId){
        return itemMapper.getItem(itemId);
    }


    public boolean isItemInStock(String itemId) {
        return true;
    }
}
