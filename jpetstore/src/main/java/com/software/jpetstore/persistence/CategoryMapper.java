package com.software.jpetstore.persistence;

import com.software.jpetstore.domain.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    List<Category> getCategoryList();
    Category getCategory(String CatagoryId);
}
