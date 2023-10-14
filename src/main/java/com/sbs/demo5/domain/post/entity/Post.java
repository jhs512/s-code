package com.sbs.demo5.domain.post.entity;


import com.sbs.demo5.base.jpa.baseEntity.BaseEntity;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.postTag.entity.PostTag;
import com.sbs.demo5.domain.textEditor.standard.TextEditorPost;
import com.sbs.demo5.standard.util.Ut;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Post extends BaseEntity implements TextEditorPost {
    @ManyToOne
    private Member author;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(columnDefinition = "TEXT")
    private String bodyHtml;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = {CascadeType.ALL})
    @Builder.Default
    @ToString.Exclude
    private Set<PostTag> postTags = new HashSet<>();

    public String getTagsStr() {
        if (postTags.isEmpty()) return "";

        return "#" + postTags
                .stream()
                .map(PostTag::getContent)
                .collect(Collectors.joining(" #"));
    }

    public void addTag(String tagContent) {
        PostTag postTag = PostTag
                .builder()
                .post(this)
                .author(this.author)
                .content(tagContent)
                .build();

        postTags.add(postTag);
    }

    public void removeTag(String tagContent) {
        postTags.removeIf(postTag -> postTag.getContent().equals(tagContent));
    }

    public String getBodyForEditor() {
        return body
                .replaceAll("(?i)(</?)script", "$1t-script");
    }

    public String getBodyHtmlForPrint() {
        return bodyHtml
                .replace("toastui-editor-ww-code-block-highlighting", "");
    }

    public void addTags(String tagsStr) {
        Arrays.stream(tagsStr.split("#|,"))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet())
                .forEach(this::addTag);
    }

    public void modifyTags(String newTagsStr) {
        Set<String> newTags = Arrays.stream(newTagsStr.split("#|,"))
                .map(String::trim)
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toSet());

        // postTags 에서 newTagsStr 에 없는 것들은 삭제
        postTags.removeIf(postTag -> !newTags.contains(postTag.getContent()));

        addTags(newTagsStr);
    }

    public String getTagLinks(String linkTemplate, String urlTemplate) {
        if (postTags.isEmpty()) return "-";

        final String finaLinkTemplate = linkTemplate.replace("`", "\"");

        return postTags
                .stream()
                .map(postTag -> finaLinkTemplate
                        .formatted(urlTemplate.formatted(Ut.url.encode(postTag.getContent())), postTag.getContent()))
                .sorted()
                .collect(Collectors.joining(" "));
    }
}

