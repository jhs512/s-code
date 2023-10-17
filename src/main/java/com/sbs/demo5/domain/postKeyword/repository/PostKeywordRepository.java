package com.sbs.demo5.domain.postKeyword.repository;

import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.postKeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {
    Optional<PostKeyword> findByAuthorAndContent(Member author, String content);
}
