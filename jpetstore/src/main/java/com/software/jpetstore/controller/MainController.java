package com.software.jpetstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    @GetMapping("/help")
    public String help() {
        return "help";
    }

    @GetMapping("/error")
    public String error(HttpSession session) {
        return "index";
    }
}
