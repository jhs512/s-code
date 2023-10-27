package com.sbs.demo5.domain.boardModule.article.entity;

import com.sbs.demo5.domain.baseModule.document.standard.DocumentHavingTags;
import com.sbs.demo5.domain.baseModule.document.standard.DocumentTag;
import com.sbs.demo5.domain.boardModule.articleTag.entity.ArticleTag;
import com.sbs.demo5.domain.boardModule.board.entity.Board;
import com.sbs.demo5.domain.memberModule.member.entity.Member;
import com.sbs.demo5.global.jpa.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity implements DocumentHavingTags {
    @ManyToOne
    private Member author;

    @ManyToOne
    private Board board;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String bodyHtml;

    @OneToMany(mappedBy = "article", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Builder.Default
    @ToString.Exclude
    private Set<ArticleTag> articleTags = new HashSet<>();

    // DocumentHavingTags 의 추상메서드
    // 태그기능을 사용하려면 필요하다.
    @Override
    public Set<? extends DocumentTag> _getTags() {
        return articleTags;
    }

    // DocumentHavingTags 의 추상메서드
    // 태그기능을 사용하려면 필요하다.
    @Override
    public DocumentTag _addTag(String tagContent) {
        ArticleTag tag = ArticleTag.builder()
                .author(author)
                .article(this)
                .content(tagContent)
                .build();

        articleTags.add(tag);

        return tag;
    }
}
