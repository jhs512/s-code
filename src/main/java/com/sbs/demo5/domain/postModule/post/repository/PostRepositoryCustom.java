package com.sbs.demo5.domain.postModule.post.repository;

import com.sbs.demo5.domain.memberModule.member.entity.Member;
import com.sbs.demo5.domain.postModule.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable);

    Page<Post> findByKw(Member author, String kwType, String kw, Pageable pageable);
}
