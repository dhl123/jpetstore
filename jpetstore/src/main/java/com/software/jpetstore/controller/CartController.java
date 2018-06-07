package com.software.jpetstore.controller;

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

import javax.servlet.http.HttpSession;
import java.util.Iterator;

@Controller
public class CartController {
    private final CatalogService catalogService;
    private final AccountService accountService;

    @Autowired
    public CartController(CatalogService catalogService, AccountService accountService) {
        this.catalogService = catalogService;
        this.accountService = accountService;
    }

    @GetMapping("cart/addItemToCart")
    public String addItem(@RequestParam("workingItemId") String itemid, HttpSession session, Model model){
        boolean isInStock = catalogService.isItemInStock(itemid);
        Item item = catalogService.getItem(itemid);
        ((Cart)session.getAttribute("cart")).addItem(item, isInStock);
        // model.addAttribute(item);
        // model.addAttribute(item.getProduct());
        return "redirect:/cart/cart";
    }
    @GetMapping("cart/viewCart")
    public String viewCart(HttpSession session,Model model){
        model.addAttribute(session.getAttribute("cart"));
        Iterator<CartItem> cartItems=((Cart)session.getAttribute("cart")).getCartItems();
        model.addAttribute(cartItems);
        return "cart/cart";
    }
    @GetMapping("cart/cart")
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
            return "cart/cart";
        }
    }
    @GetMapping("/cart/updateCartQuantities")
    public String update_cart(HttpSession session){
        Iterator<CartItem> cartItems = ((Cart)session.getAttribute("cart")).getAllCartItems();
        while (cartItems.hasNext()) {
            CartItem cartItem = cartItems.next();
            String itemId = cartItem.getItem().getItemId();
            try {
                int quantity = Integer.parseInt(itemId);
                ((Cart)session.getAttribute("cart")).setQuantityByItemId(itemId, quantity);
                if (quantity < 1) {
                    cartItems.remove();
                }
            } catch (Exception e) {
                //ignore parse exceptions on purpose
            }
        }
        return "cart/cart";
    }
}
