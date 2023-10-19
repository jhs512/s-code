package com.sbs.demo5.domain.post.entity;


import com.sbs.demo5.base.jpa.baseEntity.BaseEntity;
import com.sbs.demo5.domain.document.standard.DocumentHavingSortableTags;
import com.sbs.demo5.domain.document.standard.DocumentSortableTag;
import com.sbs.demo5.domain.document.standard.DocumentTag;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.postTag.entity.PostTag;
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
public class Post extends BaseEntity implements DocumentHavingSortableTags {
    @ManyToOne
    private Member author;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String bodyHtml;

    private boolean isPublic;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Builder.Default
    @ToString.Exclude
    private Set<PostTag> postTags = new HashSet<>();

    @Override
    public Set<? extends DocumentSortableTag> _getTags() {
        return postTags;
    }

    @Override
    public DocumentTag _addTag(String tagContent) {
        PostTag tag = PostTag.builder()
                .author(author)
                .post(this)
                .content(tagContent)
                .build();

        postTags.add(tag);

        return tag;
    }

    public String getPublicHanName() {
        return isPublic ? "공개" : "비공개";
    }
}

