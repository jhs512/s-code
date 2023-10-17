package com.sbs.demo5.domain.postKeyword.entity;

import com.sbs.demo5.base.jpa.baseEntity.BaseEntity;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.postTag.entity.PostTag;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class PostKeyword extends BaseEntity {
    @ManyToOne
    private Member author;
    private String content;

    @OneToMany(mappedBy = "postKeyword")
    @Builder.Default
    @ToString.Exclude
    @OrderBy("sortNo ASC")
    private Set<PostTag> postTags = new LinkedHashSet<>();

    private long total;

    public void addTag(PostTag postTag) {
        postTag.setPostKeyword(this);
        boolean added = postTags.add(postTag);

        if (added) {
            postTag.setSortNo(total + 1);
            total++;
        }
    }

    public void removeTag(PostTag postTag) {
        postTags.remove(postTag);
        total--;
    }

    public void applySortNo(PostTag postTag, long newSortNo) {
        long oldSortNo = postTag.getSortNo();

        if (oldSortNo == newSortNo) return;

        if (newSortNo < oldSortNo)
            postTags.stream().filter(t -> t.getSortNo() >= newSortNo).forEach(t -> t.setSortNo(t.getSortNo() + 1));
        else
            postTags.stream().filter(t -> t.getSortNo() <= newSortNo).forEach(t -> t.setSortNo(t.getSortNo() - 1));

        postTag.setSortNo(newSortNo);

        List<PostTag> _postTags = postTags.stream().sorted(Comparator.comparingLong(PostTag::getSortNo)).toList();

        postTags.clear();

        IntStream.rangeClosed(1, _postTags.size()).forEach(no -> {
            PostTag _postTag = _postTags.get(no - 1);
            _postTag.setSortNo(no);
            postTags.add(_postTag);
        });
    }
}
