package com.software.jpetstore.controller;

import com.software.jpetstore.domain.Account;
import com.software.jpetstore.domain.Cart;
import com.software.jpetstore.domain.Order;
import com.software.jpetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    private static final List<String> CARD_TYPE_LIST;
    private Order order = new Order();
    private boolean shippingAddressRequired;
    private boolean confirmed;
    private List<Order> orderList;
    static {
        List<String> cardList = new ArrayList<>();
        cardList.add("Visa");
        cardList.add("MasterCard");
        cardList.add("American Express");
        CARD_TYPE_LIST = Collections.unmodifiableList(cardList);
    }

    public int getOrderId() {
        return order.getOrderId();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public boolean isShippingAddressRequired() {
        return shippingAddressRequired;
    }

    public void setShippingAddressRequired(boolean shippingAddressRequired) {
        this.shippingAddressRequired = shippingAddressRequired;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public List<String> getCreditCardTypes() {
        return CARD_TYPE_LIST;
    }

    public List<Order> getOrderList() {
        return orderList;
    }







    @GetMapping("/order/newOrderForm")
    public String newOrder(HttpSession session){
    Account account=((Account)session.getAttribute("account"));
    Cart cart=((Cart)session.getAttribute("cart"));
        if (account == null || !((boolean)session.getAttribute("authenticated"))) {
            //setMessage("You must sign on before attempting to check out.  Please sign on and try checking out again.");
            return "/help.html";
        } else if (cart != null) {
            order.initOrder(account, cart);
            return "/order/newOrderForm";
        } else {
            //setMessage("An order could not be created because a cart could not be found.");
            return "/help.html";
        }
    }

    @RequestMapping(value = "/order/new", method= RequestMethod.POST)
    public String New(HttpSession session,@ModelAttribute(value="order") Order order_new){
    order=order_new;
        if (shippingAddressRequired) {
            shippingAddressRequired = false;
            return "/order/ShippingForm";
        } else if (!isConfirmed()) {
            return "/order/ConfirmOrder";
        } else if (getOrder() != null) {

            orderService.insertOrder(order);
          //  Cart cart=((Cart)session.getAttribute("cart"));

            //setMessage("Thank you, your order has been submitted.");

            return "/order/ViewOrder";
        } else {
            //setMessage("An error occurred processing your order (order was null).");
            return "/help.html";
        }
    }
    @RequestMapping(value = "/order/ShippingForm", method= RequestMethod.POST)
    public String shipping(@ModelAttribute(value="order") Order order_new){
        order.setShipAddress1(order_new.getShipAddress1());
        order.setShipAddress2(order_new.getShipAddress2());
        order.setShipCity(order_new.getShipCity());
        order.setShipToFirstName(order_new.getShipToFirstName());
        order.setShipToLastName(order_new.getShipToLastName());
        order.setShipCountry(order_new.getShipCountry());
        order.setShipState(order_new.getShipState());
        order.setShipZip(order_new.getShipZip());

        return "/order/new";
    }
}
