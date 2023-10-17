package com.sbs.demo5.domain.post.entity;


import com.sbs.demo5.base.jpa.baseEntity.BaseEntity;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.post.service.PostService;
import com.sbs.demo5.domain.postKeyword.entity.PostKeyword;
import com.sbs.demo5.domain.postTag.entity.PostTag;
import com.sbs.demo5.domain.textEditor.standard.TextEditorPost;
import com.sbs.demo5.standard.util.Ut;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
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
    @OrderBy("id ASC")
    private Set<PostTag> postTags = new LinkedHashSet<>();

    public String getTagsStr() {
        if (postTags.isEmpty()) return "";

        return "#" + postTags
                .stream()
                .map(PostTag::getContent)
                .sorted()
                .collect(Collectors.joining(" #"));
    }

    public String getTagsWithSortNoStr() {
        if (postTags.isEmpty()) return "";

        return "#" + postTags
                .stream()
                .map(postTag -> postTag.getContent() + "[" + postTag.getSortNo() + "/" + postTag.getPostKeyword().getTotal() + "]")
                .sorted()
                .collect(Collectors.joining(" #"));
    }

    public void addTag(String tagContent, Map<String, PostKeyword> postKeywordsMap) {
        PostTag postTag = PostTag
                .builder()
                .post(this)
                .author(this.author)
                .content(tagContent)
                .build();
        postKeywordsMap.get(tagContent).addTag(postTag);
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

    public void modifyTags(String newTagsStr, Map<String, PostKeyword> postKeywordsMap) {
        String inputedNewTagsStr = newTagsStr;
        newTagsStr = newTagsStr.replaceAll(PostService.tagsStrSortRegex, "");

        Set<String> newTags = Arrays.stream(newTagsStr.split(PostService.tagsStrDivisorRegex))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(tagContent -> !tagContent.isEmpty())
                .collect(Collectors.toSet());

        // postTags 에서 newTagsStr 에 없는 것들은 삭제
        postTags.removeIf(postTag -> {
            boolean remove = !newTags.contains(postTag.getContent());

            if (remove) postTag.getPostKeyword().removeTag(postTag);

            return remove;
        });

        addTags(inputedNewTagsStr, postKeywordsMap);
    }

    public void addTags(String tagsStr, Map<String, PostKeyword> postKeywordsMap) {
        String inputedTagsStr = tagsStr;
        tagsStr = tagsStr.replaceAll(PostService.tagsStrSortRegex, "");

        Arrays.stream(tagsStr.split(PostService.tagsStrDivisorRegex))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(tagContent -> !tagContent.isEmpty())
                .distinct()
                .forEach(tagContent -> addTag(tagContent, postKeywordsMap));

        Arrays.stream(inputedTagsStr.split(PostService.tagsStrDivisorRegex))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(tagContent -> !tagContent.isEmpty())
                .distinct()
                .forEach(tagContent -> {
                    String[] tagContentBits = tagContent.split("\\[", 2);

                    if (tagContentBits.length == 1) return;

                    tagContent = tagContentBits[0];

                    tagContentBits = tagContentBits[1].split("/", 2);

                    long newSortNo = 0;

                    try {
                        newSortNo = Long.parseLong(tagContentBits[0].replace("]", "").trim());
                    } catch (Exception ignored) {
                        return;
                    }

                    if (newSortNo < 1) newSortNo = 1;
                    if (newSortNo > postKeywordsMap.get(tagContent).getTotal())
                        newSortNo = postKeywordsMap.get(tagContent).getTotal();

                    final long _newSortNo = newSortNo;
                    final String _tagContent = tagContent;

                    postTags
                            .stream()
                            .filter(postTag -> postTag.getContent().equals(_tagContent))
                            .findFirst()
                            .ifPresent(postTag -> postTag.applySortNo(_newSortNo));
                });
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

