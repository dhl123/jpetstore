package com.software.jpetstore.controller;

import com.software.jpetstore.domain.Account;
import com.software.jpetstore.domain.Product;
import com.software.jpetstore.service.AccountService;
import com.software.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AccountController {
    private final AccountService accountService;
    private final CatalogService catalogService;

    @Autowired
    public AccountController(AccountService accountService, CatalogService catalogService) {
        this.accountService = accountService;
        this.catalogService = catalogService;
    }

    @GetMapping("/account/SignonForm")
    public String view(){
        return "/account/SignonForm";
    }
    @RequestMapping(value = "/account/login",method = RequestMethod.POST)
    public String search(@RequestParam("username") String username,@RequestParam("password") String password, HttpSession session,Model model){
        Account account=accountService.getAccount(username,password);
        if (account== null) {
            String value = "Invalid username or password.  Signon failed.";
            //alert??????????;
            return "/help.html";

        } else {
            List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            boolean authenticated=true;
            session.setAttribute("myList",myList);
            session.setAttribute("authenticated",authenticated);
            session.setAttribute("account",account);
            return "redirect:/account/SignonForm";
        }
    }
}
