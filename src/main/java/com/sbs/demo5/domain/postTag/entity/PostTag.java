package com.sbs.demo5.domain.postTag.entity;

import com.sbs.demo5.base.jpa.baseEntity.BaseEntity;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.post.entity.Post;
import com.sbs.demo5.domain.postKeyword.entity.PostKeyword;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class PostTag extends BaseEntity {
    @ManyToOne
    private Member author;

    @ManyToOne
    private Post post;

    @ManyToOne
    private PostKeyword postKeyword;

    private String content;

    private long sortNo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PostTag that = (PostTag) o;

        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    public void applySortNo(long newSortNo) {
        if (sortNo == newSortNo) return;

        postKeyword.applySortNo(this, newSortNo);
    }
}
