package com.sbs.demo5.domain.article.controller;

import com.sbs.demo5.base.rq.Rq;
import com.sbs.demo5.base.rsData.RsData;
import com.sbs.demo5.domain.article.entity.Article;
import com.sbs.demo5.domain.article.service.ArticleService;
import com.sbs.demo5.domain.board.entity.Board;
import com.sbs.demo5.domain.board.service.BoardService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/usr/article")
@RequiredArgsConstructor
public class ArticleController {
    private final BoardService boardService;
    private final ArticleService articleService;
    private final Rq rq;

    @GetMapping("/{boardCode}/list")
    public String showList(
            Model model,
            @PathVariable String boardCode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "all") String kwType
    ) {
        Board board = boardService.findByCode(boardCode).get();

        model.addAttribute("board", board);

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));
        Page<Article> articlePage = articleService.findByKw(board, kwType, kw, pageable);
        model.addAttribute("articlePage", articlePage);

        return "usr/article/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{boardCode}/write")
    public String showWrite(
            Model model,
            @PathVariable String boardCode
    ) {
        Board board = boardService.findByCode(boardCode).get();

        model.addAttribute("board", board);

        return "usr/article/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{boardCode}/write")
    public String write(
            Model model,
            @PathVariable String boardCode,
            ArticleWriteForm articleWriteForm
    ) {
        Board board = boardService.findByCode(boardCode).get();

        RsData<Article> rsData = articleService.write(board, rq.getMember(), articleWriteForm.getSubject(), articleWriteForm.getBody());

        return rq.redirectOrBack("/usr/article/%s/detail/%d".formatted(board.getCode(), rsData.getData().getId()), rsData);
    }

    @AllArgsConstructor
    @Getter
    public static class ArticleWriteForm {
        private String subject;
        private String body;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{boardCode}/detail/{id}")
    public String showDetail(
            Model model,
            @PathVariable String boardCode,
            @PathVariable long id
    ) {
        Board board = boardService.findByCode(boardCode).get();
        Article article = articleService.findById(id).get();

        model.addAttribute("board", board);
        model.addAttribute("article", article);

        return "usr/article/detail";
    }
}
