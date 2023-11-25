package com.ll.domain.postModule.post.service;

import com.ll.domain.baseModule.document.service.DocumentService;
import com.ll.domain.baseModule.document.standard.DocumentHavingSortableTags;
import com.ll.domain.baseModule.genFile.entity.GenFile;
import com.ll.domain.baseModule.genFile.service.GenFileService;
import com.ll.domain.memberModule.member.entity.Member;
import com.ll.domain.postModule.post.entity.Post;
import com.ll.domain.postModule.post.repository.PostRepository;
import com.ll.domain.postModule.postKeyword.entity.PostKeyword;
import com.ll.domain.postModule.postKeyword.repository.PostKeywordRepository;
import com.ll.global.app.AppConfig;
import com.ll.global.rsData.RsData;
import com.ll.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final GenFileService genFileService;
    private final DocumentService documentService;

    @Transactional
    public RsData<Post> write(Member author, String subject, String tagsStr, String body, boolean isPublic) {
        return write(author, subject, tagsStr, body, Ut.markdown.toHtml(body), isPublic);
    }

    @Transactional
    public RsData<Post> write(Member author, String subject, String tagsStr, String body, String bodyHtml, boolean isPublic) {
        Post post = Post.builder()
                .author(author)
                .subject(subject)
                .body(body)
                .bodyHtml(bodyHtml)
                .isPublic(isPublic)
                .build();

        postRepository.save(post);

        Map<String, PostKeyword> postKeywordsMap = findPostKeywordsMap(author, tagsStr);
        post.addTags(tagsStr, postKeywordsMap);

        documentService.updateTempGenFilesToInBody(post);

        return new RsData<Post>("S-1", post.getId() + "번 글이 생성되었습니다.", post);
    }

    public Page<Post> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable) {
        return postRepository.findByKw(kwType, kw, isPublic, pageable);
    }

    public Page<Post> findByKw(Member author, String kwType, String kw, Pageable pageable) {
        return postRepository.findByKw(author, kwType, kw, pageable);
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public RsData<?> checkActorCanModify(Member actor, Post post) {
        if (actor == null || !actor.equals(post.getAuthor())) {
            return new RsData<>("F-1", "권한이 없습니다.", null);
        }

        return new RsData<>("S-1", "가능합니다.", null);
    }

    public RsData<?> checkActorCanRemove(Member actor, Post post) {
        return checkActorCanModify(actor, post);
    }

    @Transactional
    public RsData<Post> modify(Post post, String subject, String tagsStr, String body, String bodyHtml, boolean isPublic) {

        Map<String, PostKeyword> postKeywordsMap = findPostKeywordsMap(post.getAuthor(), tagsStr);
        post.modifyTags(tagsStr, postKeywordsMap);

        post.setSubject(subject);
        modifyBody(post, body, bodyHtml);
        post.setPublic(isPublic);

        return new RsData<>("S-1", post.getId() + "번 글이 수정되었습니다.", post);
    }

    private Map<String, PostKeyword> findPostKeywordsMap(Member author, String tagsStr) {
        tagsStr = tagsStr.replaceAll(DocumentHavingSortableTags.TAGS_STR_SORT_REGEX, "");

        return Arrays.stream(tagsStr.split(DocumentHavingSortableTags.TAGS_STR_DIVISOR_REGEX))
                .map(String::trim)
                .map(String::toUpperCase)
                .filter(tag -> !tag.isEmpty())
                .distinct()
                .map(tag -> postKeywordRepository.findByAuthorAndContent(author, tag)
                        .orElseGet(() -> postKeywordRepository.save(PostKeyword.builder()
                                .author(author)
                                .content(tag)
                                .build())))
                .collect(Collectors.toMap(PostKeyword::getContent, postKeyword -> postKeyword));
    }

    @Transactional
    public RsData<?> remove(Post post) {
        findGenFiles(post).forEach(genFileService::remove);

        postRepository.delete(post);

        return new RsData<>("S-1", post.getId() + "번 게시물이 삭제되었습니다.", null);
    }

    private List<GenFile> findGenFiles(Post post) {
        return genFileService.findByRelId(post.getModelName(), post.getId());
    }

    @Transactional
    public RsData<GenFile> saveAttachmentFile(Post post, MultipartFile attachmentFile, long fileNo) {
        String attachmentFilePath = Ut.file.toFile(attachmentFile, AppConfig.getTempDirPath());
        return saveAttachmentFile(post, attachmentFilePath, fileNo);
    }

    @Transactional
    public RsData<GenFile> saveAttachmentFile(Post post, String attachmentFile, long fileNo) {
        GenFile genFile = genFileService.save(post.getModelName(), post.getId(), "global", "attachment", fileNo, attachmentFile);

        return new RsData<>("S-1", genFile.getId() + "번 파일이 생성되었습니다.", genFile);
    }

    public Map<String, GenFile> findGenFilesMapKeyByFileNo(Post post, String typeCode, String type2Code) {
        return genFileService.findGenFilesMapKeyByFileNo(post.getModelName(), post.getId(), typeCode, type2Code);
    }

    @Transactional
    public void removeAttachmentFile(Post post, long fileNo) {
        genFileService.remove(post.getModelName(), post.getId(), "global", "attachment", fileNo);
    }

    public Page<Post> findByTag(String tagContent, boolean isPublic, Pageable pageable) {
        return postRepository.findByPostTags_contentAndIsPublic(tagContent, isPublic, pageable);
    }

    public Page<Post> findByTag(Member author, String tagContent, Pageable pageable) {
        return postRepository.findByAuthorAndPostTags_content(author, tagContent, pageable);
    }

    public Page<Post> findByTag(Member author, String tagContent, boolean isPublic, Pageable pageable) {
        return postRepository.findByAuthorAndPostTags_contentAndIsPublic(author, tagContent, isPublic, pageable);
    }

    public Optional<PostKeyword> findKeywordById(long postKeywordId) {
        return postKeywordRepository.findById(postKeywordId);
    }

    public List<PostKeyword> findPostKeywordsByMemberId(Member actor) {
        return postKeywordRepository.findByAuthorOrderByContent(actor);
    }

    public Optional<PostKeyword> findPostKeywordById(long postKeywordId) {
        return postKeywordRepository.findById(postKeywordId);
    }

    @Transactional
    public RsData<Post> modifyBody(Post post, String body, String bodyHtml) {
        post.setBody(body);
        post.setBodyHtml(bodyHtml);

        documentService.updateTempGenFilesToInBody(post);

        return new RsData<>("S-1", post.getId() + "번 글의 내용이 수정되었습니다.", post);
    }

    public List<Post> findByAddi2(String username) {
        return postRepository.findByAddi2(username);
    }
}
