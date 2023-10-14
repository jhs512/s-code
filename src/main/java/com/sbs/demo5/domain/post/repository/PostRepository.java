package com.sbs.demo5.domain.post.repository;

import com.sbs.demo5.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Page<Post> findByPostTags_content(String tagContent, Pageable pageable);
}
