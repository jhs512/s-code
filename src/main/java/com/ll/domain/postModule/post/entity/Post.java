package com.ll.domain.postModule.post.entity;


import com.ll.domain.baseModule.document.standard.DocumentHavingSortableTags;
import com.ll.domain.baseModule.document.standard.DocumentSortableTag;
import com.ll.domain.baseModule.document.standard.DocumentTag;
import com.ll.domain.memberModule.member.entity.Member;
import com.ll.domain.postModule.postTag.entity.PostTag;
import com.ll.global.jpa.baseEntity.BaseEntity;
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

    @Column(columnDefinition = "LONGTEXT")
    private String body;

    @Column(columnDefinition = "LONGTEXT")
    private String bodyHtml;

    private String addi1;
    private String addi2;

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

    public String getSubjectForPrint() {
        return subject;
    }

    public String getPublicHanName() {
        return isPublic ? "공개" : "비공개";
    }
}

