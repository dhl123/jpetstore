package com.software.jpetstore.controller;

import com.software.jpetstore.domain.Account;
import com.software.jpetstore.domain.Cart;
import com.software.jpetstore.domain.Order;
import com.software.jpetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class OrderController {
    private final OrderService orderService;
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

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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


    @GetMapping("/order/NewOrderForm")
    public String newOrder(HttpSession session, Model model){
    Account account=((Account)session.getAttribute("account"));
    Cart cart=((Cart)session.getAttribute("cart"));
        if (account == null || !((boolean)session.getAttribute("authenticated"))) {
            //setMessage("You must sign on before attempting to check out.  Please sign on and try checking out again.");
            return "/help.html";
        } else if (cart != null) {
            order.initOrder(account, cart);
            List<String> creditCardTypes=getCreditCardTypes();
            System.out.println(creditCardTypes);
            model.addAttribute(creditCardTypes);
            return "/order/NewOrderForm";
        } else {
            //setMessage("An order could not be created because a cart could not be found.");
            return "/help.html";
        }
    }


    @RequestMapping(value = "/order/new",method = RequestMethod.POST)
    public String New(HttpSession session,@RequestParam("ShipAddress1") String ShipAddress1,@RequestParam("ShipAddress2") String ShipAddress2,
                      @RequestParam("ShipCity") String ShipCity,@RequestParam("ShipToFirstName") String ShipToFirstName,
                      @RequestParam("ShipToLastName") String ShipToLastName, @RequestParam("ShipCountry") String ShipCountry,
                      @RequestParam("ShipState") String ShipState,@RequestParam("ShipZip") String ShipZip){



        if (!isConfirmed()) {
            return "/order/ConfirmOrder";
        } else if (getOrder() != null) {
            order.setShipAddress1(ShipAddress1);
            order.setShipAddress2(ShipAddress2);
            order.setShipCity(ShipCity);
            order.setShipToFirstName(ShipToFirstName);
            order.setShipToLastName(ShipToLastName);
            order.setShipCountry(ShipCountry);
            order.setShipState(ShipState);
            order.setShipZip(ShipZip);

            orderService.insertOrder(order);
          //  Cart cart=((Cart)session.getAttribute("cart"));

            //setMessage("Thank you, your order has been submitted.");

            return "/order/ViewOrder";
        } else {
            //setMessage("An error occurred processing your order (order was null).");
            return "/help.html";
        }



    }
    @RequestMapping(value = "/order/ShippingForm",method = RequestMethod.POST)
    public String shipping(@RequestParam("cardType") String cardType,@RequestParam("creditCard") String  creditCard,
                           @RequestParam("expiryDate") String  expiryDate,@RequestParam("billToFirstName") String  billToFirstName,
                           @RequestParam("billToLastName") String  billToLastName,@RequestParam("billAddress1") String  billAddress1,
                           @RequestParam("billAddress2") String  billAddress2,@RequestParam("billCity") String  billCity,
                           @RequestParam("billState") String  billState,@RequestParam("billZip") String  billZip,
                           @RequestParam("billCountry") String  billCountry,@RequestParam("shippingAddressRequired") Boolean  shippingAddressRequired_){
        order.setCardType(cardType);
        order.setCreditCard(creditCard);
        order.setExpiryDate(expiryDate);
        order.setBillToFirstName(billToFirstName);
        order.setBillToLastName(billToLastName);
        order.setBillAddress1(billAddress1);
        order.setBillAddress2(billAddress2);
        order.setBillCity(billCity);
        order.setStatus(billState);
        order.setBillZip(billZip);
        order.setBillCountry(billCountry);
        shippingAddressRequired=shippingAddressRequired_;
        return "/order/new";
    }
}
