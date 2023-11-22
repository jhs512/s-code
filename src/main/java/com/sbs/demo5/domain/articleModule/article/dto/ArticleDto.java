package com.sbs.demo5.domain.articleModule.article.dto;

import com.sbs.demo5.domain.articleModule.article.entity.Article;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArticleDto {
    private final long id;
    private final LocalDateTime createDate;
    private final LocalDateTime modifyDate;
    private final Long authorId;
    private final String authorName;
    private final Long boardId;
    private final String boardCode;
    private final String boardName;
    private final String subject;
    private final String body;
    private final String bodyHtml;
    private final String[] tagsStr;

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.createDate = article.getCreateDate();
        this.modifyDate = article.getModifyDate();
        this.authorId = article.getAuthor().getId();
        this.authorName = article.getAuthor().getNickname();
        this.boardId = article.getBoard().getId();
        this.boardCode = article.getBoard().getCode();
        this.boardName = article.getBoard().getName();
        this.subject = article.getSubject();
        this.body = article.getBody();
        this.bodyHtml = article.getBodyHtml();
        this.tagsStr = article.getTagsArr();
    }
}
