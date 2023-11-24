package com.ll.domain.postModule.postViewer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/p")
@RequiredArgsConstructor
public class UsrPostViewerController {
    @GetMapping("/{id}")
    public String goTo(@PathVariable long id, Model model) {
        return "redirect:/p/" + id;
    }
}
