package com.software.jpetstore.controller;

import com.software.jpetstore.domain.Category;
import com.software.jpetstore.domain.Item;
import com.software.jpetstore.domain.Product;
import com.software.jpetstore.persistence.ProductMapper;
import com.software.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @GetMapping("/catalog/product")
    public String viewProduct(@RequestParam("productId") String productid, Model model){
        //System.out.println(productid);
        Product product=catalogService.getProduct(productid);
        List<Item> itemList =catalogService.getItemListByProduct(productid);
        model.addAttribute(product);
        model.addAttribute(itemList);
        return "/catalog/product";
    }
    @GetMapping("/catalog/item")
    public String viewItem(@RequestParam("itemId") String itemid,@RequestParam("productId") String productid, Model model){
        Item item=catalogService.getItem(itemid);
        Product product=catalogService.getProduct(productid);

        model.addAttribute(item);
        model.addAttribute(product);
        return "/catalog/item";
    }
   // @GetMapping("/catalog/SearchProducts")
    @RequestMapping(value = "/catalog/SearchProducts",method = RequestMethod.POST)
    public String search(@RequestParam("keyword") String keyword,Model model){
        List<Product> productList=catalogService.searchProductList(keyword);
        model.addAttribute(productList);
        return "/catalog/SearchProducts";
    }
}
