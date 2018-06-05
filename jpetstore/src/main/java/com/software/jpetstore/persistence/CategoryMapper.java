package com.software.jpetstore.persistence;

import com.software.jpetstore.domain.Category;

import java.util.List;

public interface CategoryMapper {
    List<Category> getCategoryList();
    Category getCategory(String CatagoryId);
}
