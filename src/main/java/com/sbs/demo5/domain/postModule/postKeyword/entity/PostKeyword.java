package com.sbs.demo5.domain.postModule.postKeyword.entity;

import com.sbs.demo5.domain.baseModule.document.standard.DocumentSortableKeyword;
import com.sbs.demo5.domain.baseModule.document.standard.DocumentSortableTag;
import com.sbs.demo5.domain.memberModule.member.entity.Member;
import com.sbs.demo5.domain.postModule.postTag.entity.PostTag;
import com.sbs.demo5.global.jpa.baseEntity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class PostKeyword extends BaseEntity implements DocumentSortableKeyword {
    @ManyToOne
    private Member author;
    private String content;

    // 배경지식 시작
    // 아래 @OneToMany 옵션에 ophanRemoval = true, cascade = CascadeType.ALL 가 없는 이유
    // PostKeyword 는 PostTag 들의 순서만 관리하는 역할을 한다.
    // PostTag 의 삭제와 추가는 Post 엔티티가 관리한다.
    // 배경지식 끝
    @OneToMany(mappedBy = "postKeyword")
    @Builder.Default
    @ToString.Exclude
    @OrderBy("sortNo ASC")
    private Set<PostTag> postTags = new LinkedHashSet<>();

    @Setter(PRIVATE)
    private long total;

    @Override
    public Set<? extends DocumentSortableTag> _getTags() {
        return postTags;
    }

    @Override
    public boolean __addTag(DocumentSortableTag tag) {
        return postTags.add((PostTag) tag);
    }

    @Override
    public void _setTotal(long total) {
        setTotal(total);
    }
}
