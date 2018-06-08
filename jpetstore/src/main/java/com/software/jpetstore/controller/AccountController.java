package com.software.jpetstore.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.software.jpetstore.domain.Account;
import com.software.jpetstore.domain.Category;
import com.software.jpetstore.domain.Product;
import com.software.jpetstore.service.AccountService;
import com.software.jpetstore.service.CatalogService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
    @Autowired
    DefaultKaptcha defaultKaptcha;

    @PostMapping("/account/newAccount")
    public String newAccount(HttpSession session,
                             @RequestParam("kaptcha") String kaptcha,
                             @RequestParam("username") String username,
                             @RequestParam("password") String password, @RequestParam("repeatedPassword") String repeatedPassword,
                             @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                             @RequestParam("languagePreference") String languagePreference,
                             @RequestParam("email") String email, @RequestParam("phone") String phone,
                             @RequestParam("address1") String address1, @RequestParam("address2") String address2,
                             @RequestParam("city") String city, @RequestParam("state") String state,
                             @RequestParam("zip") String zip, @RequestParam("country") String country,
                             @RequestParam("favouriteCategoryId") String favouriteCategoryId,
                             @RequestParam(name = "listOption", defaultValue = "true") Boolean listOption,
                             @RequestParam(name = "bannerOption", defaultValue = "true") Boolean bannerOption) {

        String captchaSess = (String) session.getAttribute("verifyCode");
        if (!captchaSess.equals(kaptcha)) {
            session.setAttribute("errorMessage", "Wrong captcha.");
            return "error";
        }

        if (!password.equals(repeatedPassword)) {
            session.setAttribute("errorMessage", "password not equal repeatepassword");
            return "error";
        }

        if (accountService.getAccount(username) != null) {
            session.setAttribute("errorMessage", "Duplicate account existed.");
            return "error";
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setLanguagePreference(languagePreference);
        account.setEmail(email);
        account.setPhone(phone);
        account.setAddress1(address1);
        account.setAddress2(address2);
        account.setCity(city);
        account.setState(state);
        account.setZip(zip);
        account.setCountry(country);
        account.setStatus("OK");
        account.setFavouriteCategoryId(favouriteCategoryId);
        account.setBannerOption(bannerOption);
        account.setListOption(listOption);
        accountService.insertAccount(account);
        session.setAttribute("account", account);
        List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
        session.setAttribute("myList", myList);
        session.setAttribute("authenticated", true);

        return "redirect:/catalog/main";
    }
    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
            byte[] captchaChallengeAsJpeg;
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            try {
                String createText = defaultKaptcha.createText();
                httpServletRequest.getSession().setAttribute("verifyCode", createText);
                BufferedImage challenge = defaultKaptcha.createImage(createText);
                ImageIO.write(challenge, "jpg", jpegOutputStream);
            } catch (IllegalArgumentException e) {
                httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            httpServletResponse.setHeader("Cache-Control", "no-store");
            httpServletResponse.setHeader("Pragma", "no-cache");
            httpServletResponse.setDateHeader("Expires", 0);
            httpServletResponse.setContentType("image/jpeg");
            ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
            responseOutputStream.write(captchaChallengeAsJpeg);
            responseOutputStream.flush();
            responseOutputStream.close();
        }



    @GetMapping("/account/SignonForm")
    public String view(){
        return "/account/SignonForm";
    }
    @RequestMapping(value = "/account/login",method = RequestMethod.POST)
    public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session, Model model) {
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
            return "catalog/main";
        }
    }

    @GetMapping("/account/signoff")
    public String signoff(HttpSession session) {
        session.setAttribute("account", null);
        session.setAttribute("myL ist", null);
        session.setAttribute("authenticated", false);
        return "redirect:/catalog/main";
    }
    @GetMapping("/account/EditAccountForm")
    public String editform(){
        return "/account/EditAccountForm";
    }
    @RequestMapping(value = "/account/edit",method = RequestMethod.POST)
    public String editAccount(@RequestParam("password") String password, @RequestParam("repeatedPassword") String repeatedPassword,
                              @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                              @RequestParam("email") String email, @RequestParam("phone") String phone,
                              @RequestParam("address1") String address1, @RequestParam("address2") String address2,
                              @RequestParam("city") String city, @RequestParam("state") String state, @RequestParam("languagePreference") String languagePreference,
                              @RequestParam("favouriteCategoryId") String favouriteCategoryId,
                              @RequestParam(name = "listOption", defaultValue = "true") Boolean listOption,
                              @RequestParam(name = "bannerOption", defaultValue = "true") Boolean bannerOption,
                              @RequestParam("zip") String zip, @RequestParam("country") String country, HttpSession session, Model model) {

        if(!password.equals(repeatedPassword)){
            session.setAttribute("errorMessage", "password not equal repeatepassword");
            return "error";
        }
        Account account=(Account)session.getAttribute("account");
        account.setPassword(password);
        account.setFirstName(firstName);
        account.setLanguagePreference(lastName);
        account.setEmail(email);
        account.setPhone(phone);
        account.setLanguagePreference(languagePreference);
        account.setAddress1(address1);
        account.setAddress2(address2);
        account.setCity(city);
        account.setState(state);
        account.setZip(zip);
        account.setCountry(country);
        account.setFavouriteCategoryId(favouriteCategoryId);
        account.setBannerOption(bannerOption);
        account.setListOption(listOption);

        accountService.updateAccount(account);
        session.setAttribute("account",account);
        List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
        session.setAttribute("myList",myList);
        return "/account/EditAccountForm";
    }

    @GetMapping("/account/newAccountForm")
    public String newForm() {
        return "account/NewAccountForm";
    }

    @GetMapping("/account/check_account")
    @ResponseBody
    public Boolean checkAccount(@RequestParam("username") String username) {
        Account account = accountService.getAccount(username);
        return account == null;
    }


}
