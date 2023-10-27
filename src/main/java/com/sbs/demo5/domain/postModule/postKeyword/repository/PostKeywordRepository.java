package com.sbs.demo5.domain.postModule.postKeyword.repository;

import com.sbs.demo5.domain.memberModule.member.entity.Member;
import com.sbs.demo5.domain.postModule.postKeyword.entity.PostKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {
    Optional<PostKeyword> findByAuthorAndContent(Member author, String content);

    List<PostKeyword> findByAuthorOrderByContent(Member actor);
}
