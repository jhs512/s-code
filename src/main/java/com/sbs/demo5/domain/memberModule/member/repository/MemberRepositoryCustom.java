package com.sbs.demo5.domain.memberModule.member.repository;

import com.sbs.demo5.domain.memberModule.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> findByKw(String kwType, String kw, Pageable pageable);
}
