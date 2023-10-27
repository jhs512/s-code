package com.sbs.demo5.domain.postModule.post.repository;

import com.sbs.demo5.domain.memberModule.member.entity.Member;
import com.sbs.demo5.domain.postModule.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Page<Post> findByAuthorAndPostTags_content(Member author, String tagContent, Pageable pageable);

    Page<Post> findByPostTags_contentAndIsPublic(String tagContent, Boolean isPublic, Pageable pageable);

    Page<Post> findByAuthorAndPostTags_contentAndIsPublic(Member author, String tagContent, boolean isPublic, Pageable pageable);

    List<Post> findByAddi2(String username);
}
