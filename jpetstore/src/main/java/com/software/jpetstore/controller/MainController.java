package com.software.jpetstore.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Controller
public class MainController {
    @GetMapping("/changeLanguage")
    public String setLanguage(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("lang") String lang) {
        if ("zh".equals(lang)) {
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("zh", "CN"));
            LocaleContextHolder.setLocale(Locale.CHINA);
            //System.out.println("zhongwen");
        } else if ("en".equals(lang)) {
            request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, new Locale("en", "US"));
            LocaleContextHolder.setLocale(Locale.US);
        }
        return "redirect:/catalog/main";
    }


    @GetMapping("/help")
    public String help() {
        return "help";
    }

    @GetMapping("/error1")
    public String error(HttpSession session) {
        return "error1";
    }
}
