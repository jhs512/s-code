package com.ll.domain.postModule.post.repository;

import com.ll.domain.memberModule.member.entity.Member;
import com.ll.domain.postModule.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable);

    Page<Post> findByKw(Member author, String kwType, String kw, Pageable pageable);
}
