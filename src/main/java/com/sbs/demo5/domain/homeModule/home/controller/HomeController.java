package com.sbs.demo5.domain.homeModule.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String showMain(Model model) {
        model.addAttribute("pageName", "1111");
        return "usr/home/main";
    }

    @GetMapping("/ken")
    public String goToMain() {
        return "redirect:/";
    }
}
