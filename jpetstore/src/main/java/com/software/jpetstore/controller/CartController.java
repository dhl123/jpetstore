package com.software.jpetstore.controller;

import com.software.jpetstore.domain.Cart;
import com.software.jpetstore.domain.CartItem;
import com.software.jpetstore.domain.Item;
import com.software.jpetstore.domain.Product;
import com.software.jpetstore.service.AccountService;
import com.software.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

@Controller
public class CartController {
    private final CatalogService catalogService;
    private final AccountService accountService;

    @Autowired
    public CartController(CatalogService catalogService, AccountService accountService) {
        this.catalogService = catalogService;
        this.accountService = accountService;
    }

    @GetMapping("/cart/addItemToCart")
    public String addItem(@RequestParam("workingItemId") String itemid, HttpSession session, Model model){
        boolean isInStock = catalogService.isItemInStock(itemid);
        Item item = catalogService.getItem(itemid);
        ((Cart)session.getAttribute("cart")).addItem(item, isInStock);
        // model.addAttribute(item);
        // model.addAttribute(item.getProduct());
        return "redirect:/cart/viewCart";
    }
    @GetMapping("/cart/viewCart")
    public String viewCart(HttpSession session,Model model){
        model.addAttribute(session.getAttribute("cart"));
        Iterator<CartItem> cartItems=((Cart)session.getAttribute("cart")).getCartItems();
        while(cartItems.hasNext()){
            System.out.println("hello");
            CartItem cart_item = cartItems.next();
            Item item=cart_item.getItem();
            if(item.getAttribute1()==null){
                item.setAttribute1(" ");
            }
            if(item.getAttribute2()==null){
                item.setAttribute2(" ");
            }
            if(item.getAttribute3()==null){
                item.setAttribute3(" ");
            }
            if(item.getAttribute4()==null){
                item.setAttribute4(" ");
            }
            if(item.getAttribute5()==null){
                item.setAttribute5(" ");
            }
        }
        model.addAttribute(cartItems);
        return "/cart/cart";
    }
    @GetMapping("/cart/cart")
    public String view(){
        return "/cart/cart";
    }
    @GetMapping("/cart/removeItemFromCart")
    public String remove_cart(@RequestParam("cartItem") String cartitem,HttpSession session,Model model){
        Item item = ((Cart)session.getAttribute("cart")).removeItemById(cartitem);

        if (item == null) {
            //  setMessage("Attempted to remove null CartItem from Cart.");
            return "/help.html";
        } else {
            return "/cart/cart";
        }
    }
    @GetMapping("/cart/updateCartQuantities")
    public String update_cart(HttpServletRequest request, HttpSession session){

        Enumeration<String> params = request.getParameterNames();
        Iterable<String> items = Collections.list(params);
        for(String item : items) {
            String itemId = request.getParameter(item);
            int quantity = Integer.parseInt(itemId);
            ((Cart)session.getAttribute("cart")).setQuantityByItemId(item, quantity);
            if (quantity < 1) {
                ((Cart)session.getAttribute("cart")).removeItemById(item);
            }
        }
        return "redirect:/cart/viewCart";
    }

    @ResponseBody
    @PostMapping("/cart/update")
    public List <CartItem> update(HttpServletRequest request, HttpSession session){
        Enumeration<String> params = request.getParameterNames();
        Iterable<String> items = Collections.list(params);
        for(String item : items) {
            String itemId = request.getParameter(item);
            int quantity = Integer.parseInt(itemId);
            ((Cart)session.getAttribute("cart")).setQuantityByItemId(item, quantity);
            if (quantity < 1) {
                ((Cart)session.getAttribute("cart")).removeItemById(item);
            }
        }

        List <CartItem> cartItem=((Cart)session.getAttribute("cart")).getCartItemList();
        return cartItem;
    }


    @GetMapping("/cart/checkout")
    public String checkout() {
        return "/cart/checkout";
    }
}
