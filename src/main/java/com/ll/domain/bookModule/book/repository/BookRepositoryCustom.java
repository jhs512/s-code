package com.ll.domain.bookModule.book.repository;

import com.ll.domain.bookModule.book.entity.Book;
import com.ll.domain.memberModule.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookRepositoryCustom {
    Page<Book> findByKw(String kwType, String kw, boolean isPublic, Pageable pageable);

    Page<Book> findByKw(Member author, String kwType, String kw, Pageable pageable);
}
