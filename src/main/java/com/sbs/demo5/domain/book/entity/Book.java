package com.sbs.demo5.domain.book.entity;

import com.sbs.demo5.base.jpa.baseEntity.BaseEntity;
import com.sbs.demo5.domain.bookTag.entity.BookTag;
import com.sbs.demo5.domain.document.standard.DocumentHavingTags;
import com.sbs.demo5.domain.document.standard.DocumentTag;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.postKeyword.entity.PostKeyword;
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
public class Book extends BaseEntity implements DocumentHavingTags {
    @ManyToOne
    private PostKeyword postKeyword;

    @ManyToOne
    private Member author;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String bodyHtml;

    private boolean isPublic;

    @OneToMany(mappedBy = "book", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Builder.Default
    @ToString.Exclude
    private Set<BookTag> bookTags = new HashSet<>();

    // DocumentHavingTags 의 추상메서드
    // 태그기능을 사용하려면 필요하다.
    @Override
    public Set<? extends DocumentTag> _getTags() {
        return bookTags;
    }

    // DocumentHavingTags 의 추상메서드
    // 태그기능을 사용하려면 필요하다.
    @Override
    public DocumentTag _addTag(String tagContent) {
        BookTag tag = BookTag.builder()
                .author(author)
                .book(this)
                .content(tagContent)
                .build();

        bookTags.add(tag);

        return tag;
    }

    public String getPublicHanName() {
        return isPublic ? "공개" : "비공개";
    }

}


