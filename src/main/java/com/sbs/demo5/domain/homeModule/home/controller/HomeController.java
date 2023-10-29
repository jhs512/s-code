package com.sbs.demo5.domain.homeModule.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showMain() {
        return "usr/homeModule/home/main";
    }

    @GetMapping("/ken")
    public String goToMain() {
        return "redirect:/";
    }
}
