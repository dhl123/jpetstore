package com.software.jpetstore.controller;

import com.software.jpetstore.domain.Account;
import com.software.jpetstore.domain.Cart;
import com.software.jpetstore.domain.CartItem;
import com.software.jpetstore.domain.Item;
import com.software.jpetstore.service.AccountService;
import com.software.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Iterator;
import java.util.List;

@Controller
public class CartController {
    private Cart cart=new Cart();
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private AccountService accountService;
    @GetMapping("cart/addItemToCart")
    public String addItem(@RequestParam("workingItemId") String itemid,Model model){
        boolean isInStock = catalogService.isItemInStock(itemid);
        Item item = catalogService.getItem(itemid);
        cart.addItem(item, isInStock);
        model.addAttribute(item);
        model.addAttribute(item.getProduct());
        return "/catalog/item";
    }
    @GetMapping("cart/viewCart")
    public String viewCart(Model model){
        model.addAttribute(cart);
        Iterator<CartItem> cartItems=cart.getCartItems();
        model.addAttribute(cartItems);
        return "cart/cart";
    }
    @GetMapping("cart/cart")
    public String view(){
        return "/cart/cart";
    }
}
