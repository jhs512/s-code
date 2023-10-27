package com.sbs.demo5.domain.postModule.postViewer.controller;

import com.sbs.demo5.domain.postModule.post.entity.Post;
import com.sbs.demo5.domain.postModule.post.service.PostService;
import com.sbs.demo5.global.rsData.RsData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

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

    @GetMapping("/{id}/live")
    public String showDetailLive(@PathVariable long id, Model model) {
        Post post = postService.findById(id).get();

        model.addAttribute("post", post);

        return "usr/postViewer/detail";
    }

    @GetMapping("/{id}/body")
    @ResponseBody
    public RsData<ShowBodyResponse> showBody(@PathVariable long id, LocalDateTime lastModifyDate) {
        Post post = postService.findById(id).get();

        if (post.getModifyDate().isAfter(lastModifyDate)) {
            return RsData.of(
                    "S-1",
                    "새 본문을 불러옵니다.",
                    new ShowBodyResponse(
                            post.getModifyDate(),
                            post.getBodyHtml()
                    )
            );
        }

        return RsData.of("F-1", "최신 데이터가 없습니다.");
    }

    @AllArgsConstructor
    @Getter
    public static class ShowBodyResponse {
        LocalDateTime modifyDate;
        private String bodyHtml;
    }
}
