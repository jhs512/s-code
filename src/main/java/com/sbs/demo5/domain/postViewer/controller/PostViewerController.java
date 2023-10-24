package com.sbs.demo5.domain.postViewer.controller;

import com.sbs.demo5.domain.post.entity.Post;
import com.sbs.demo5.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/p")
@RequiredArgsConstructor
public class PostViewerController {
    private final PostService postService;

    @GetMapping("/{id}")
    public String showDetail(@PathVariable long id, Model model) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        return "usr/postViewer/detail";
    }
}
