package com.software.jpetstore.controller;

import com.software.jpetstore.domain.Category;
import com.software.jpetstore.domain.Product;
import com.software.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CatalogController {
    @Autowired
    private CatalogService catalogService;
    @GetMapping("/catalog/main")
    public String view(){
        return "catalog/main";
    }
    @GetMapping("/catalog/category")
    public String viewCategory(@RequestParam("categoryId") String categoryid, Model model){
        Category category=catalogService.getCategory(categoryid);
        List<Product> productList=catalogService.getProductListByCategory(categoryid);
        model.addAttribute(category);
        model.addAttribute(productList);
        return "/catalog/category";
    }
}
