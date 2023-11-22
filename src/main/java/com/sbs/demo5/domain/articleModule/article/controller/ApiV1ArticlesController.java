package com.sbs.demo5.domain.articleModule.article.controller;

import com.sbs.demo5.domain.articleModule.article.dto.ArticleDto;
import com.sbs.demo5.domain.articleModule.article.dto.BoardDto;
import com.sbs.demo5.domain.articleModule.article.entity.Article;
import com.sbs.demo5.domain.articleModule.article.service.ArticleService;
import com.sbs.demo5.domain.articleModule.board.entity.Board;
import com.sbs.demo5.domain.articleModule.board.service.BoardService;
import com.sbs.demo5.global.app.AppConfig;
import com.sbs.demo5.global.rq.Rq;
import com.sbs.demo5.global.rsData.RsData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
@RequiredArgsConstructor
@Validated
public class ApiV1ArticlesController {
    private final BoardService boardService;
    private final ArticleService articleService;
    private final Rq rq;

    @Setter
    @Getter
    public static class GetArticlesResponseBody {
        private BoardDto board;
        private Page<ArticleDto> page;

        public GetArticlesResponseBody(Board board, Page<Article> articlePage) {
            this.board = new BoardDto(board);
            this.page = articlePage.map(article -> new ArticleDto(article));
        }
    }

    @GetMapping("/{boardCode}")
    public RsData<GetArticlesResponseBody> getArticles(
            @PathVariable String boardCode,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "all") String kwType
    ) {
        Board board = boardService.findByCode(boardCode).get();

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, AppConfig.getBasePageSize(), Sort.by(sorts));
        Page<Article> articlePage = articleService.findByKw(board, kwType, kw, pageable);

        return RsData.of("S-1", "성공", new GetArticlesResponseBody(board, articlePage));
    }
}
