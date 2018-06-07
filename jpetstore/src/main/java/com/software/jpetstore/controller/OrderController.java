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


    @GetMapping("/order/newOrderForm")
    public String newOrder(HttpSession session, Model model){
    Account account=((Account)session.getAttribute("account"));
    Cart cart=((Cart)session.getAttribute("cart"));
        if (account == null || !((boolean)session.getAttribute("authenticated"))) {
            session.setAttribute("errorMessage", "You must sign on before attempting to check out.  Please sign on and try checking out again.");
            return "error";
        } else if (cart != null) {
            order.initOrder(account, cart);
            List<String> creditCardTypes=getCreditCardTypes();
            System.out.println(creditCardTypes);
            model.addAttribute("creditCardTypes", creditCardTypes);
            return "order/NewOrderForm";
        } else {
            session.setAttribute("errorMessage", "An order could not be created because a cart could not be found.");
            return "error";
        }
    }


    @RequestMapping(value = "/order/shippingForm", method = RequestMethod.POST)
    public String shippingForm(HttpSession session, Model model,
                               @RequestParam("ShipAddress1") String ShipAddress1, @RequestParam("ShipAddress2") String ShipAddress2,
                               @RequestParam("ShipCity") String ShipCity, @RequestParam("ShipToFirstName") String ShipToFirstName,
                               @RequestParam("ShipToLastName") String ShipToLastName, @RequestParam("ShipCountry") String ShipCountry,
                               @RequestParam("ShipState") String ShipState, @RequestParam("ShipZip") String ShipZip){

        order.setShipAddress1(ShipAddress1);
        order.setShipAddress2(ShipAddress2);
        order.setShipCity(ShipCity);
        order.setShipToFirstName(ShipToFirstName);
        order.setShipToLastName(ShipToLastName);
        order.setShipCountry(ShipCountry);
        order.setShipState(ShipState);
        order.setShipZip(ShipZip);
        model.addAttribute("order", order);
        return "order/NewOrderForm";
    }

    @GetMapping("/order/viewOrder")
    public String viewOrder(@RequestParam("orderId") int orderId, HttpSession session, Model model) {
        Order order = orderService.getOrder(orderId);
        Account account = (Account) session.getAttribute("account");
        if (account != null || account.getUsername().equals(order.getUsername())) {
            model.addAttribute("order", order);
            return "order/viewOrder";
        } else {
            session.setAttribute("errorMessage", "You may only view your own orders.");
            return "error";
        }
    }

    @RequestMapping(value = "/order/new", method = RequestMethod.POST)
    public String newOrder(HttpSession session, Model model,
                           @RequestParam(name = "confirmed", defaultValue = "false") Boolean confirmed,
                           @RequestParam("cardType") String cardType, @RequestParam("creditCard") String creditCard,
                           @RequestParam("expiryDate") String  expiryDate, @RequestParam("billToFirstName") String  billToFirstName,
                           @RequestParam("billToLastName") String  billToLastName, @RequestParam("billAddress1") String  billAddress1,
                           @RequestParam("billAddress2") String  billAddress2, @RequestParam("billCity") String  billCity,
                           @RequestParam("billState") String  billState, @RequestParam("billZip") String  billZip,
                           @RequestParam("billCountry") String billCountry, @RequestParam(name = "shippingAddressRequired", defaultValue = "false") Boolean shippingAddressRequired_) {
        setConfirmed(confirmed);

        shippingAddressRequired = shippingAddressRequired_;
        if (shippingAddressRequired_) {
            shippingAddressRequired = false;
            model.addAttribute("order", order);
            return "order/ShippingForm";
        }
        if (!isConfirmed()) {
            model.addAttribute("order", order);
            return "order/confirmOrder";
        } else if (getOrder() != null) {
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

            orderService.insertOrder(order);
            orderService.insertOrder(order);
            Cart cart = ((Cart) session.getAttribute("cart"));
            cart.getCartItemList().clear();

            return "order/ViewOrder";
        } else {
            session.setAttribute("errorMessage", "An error occurred processing your order (order was null).");
            return "error";
        }
    }
}
