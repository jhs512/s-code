package com.ll.domain.articleModule.article.repository;

import com.ll.domain.articleModule.article.entity.Article;
import com.ll.domain.articleModule.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {
    Page<Article> findByKw(Board board, String kwType, String kw, Pageable pageable);
}
