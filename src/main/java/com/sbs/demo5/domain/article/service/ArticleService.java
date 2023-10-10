package com.sbs.demo5.domain.article.service;


import com.sbs.demo5.base.rsData.RsData;
import com.sbs.demo5.domain.article.entity.Article;
import com.sbs.demo5.domain.article.repository.ArticleRepository;
import com.sbs.demo5.domain.board.entity.Board;
import com.sbs.demo5.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;

    public RsData<Article> write(Board board, Member author, String subject, String body) {
        Article article = Article.builder()
                .board(board)
                .author(author)
                .subject(subject)
                .body(body)
                .build();

        articleRepository.save(article);

        return new RsData<>("S-1", article.getId() + "번 게시물이 생성되었습니다.", article);
    }

    public Page<Article> findByKw(Board board, String kwType, String kw, Pageable pageable) {
        return articleRepository.findByKw(board, kwType, kw, pageable);
    }
}
