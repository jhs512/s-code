package com.sbs.demo5.domain.member.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sbs.demo5.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.sbs.demo5.domain.member.entity.QMember.member;


@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Member> dslFindAll(String kwType, String kw, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        switch (kwType) {
            case "username" -> builder.and(member.username.containsIgnoreCase(kw));
            case "nickname" -> builder.and(member.nickname.containsIgnoreCase(kw));
            case "email" -> builder.and(member.email.containsIgnoreCase(kw));
            default ->
                    builder.and(member.username.containsIgnoreCase(kw).or(member.nickname.containsIgnoreCase(kw)).or(member.email.containsIgnoreCase(kw)));
        }

        JPAQuery<Member> membersQuery = dslFindSelectFromWhere(builder);

        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(member.getType(), member.getMetadata());
            membersQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<Member> members = membersQuery.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        long total = dslFindSelectFromWhere(builder).fetchCount();

        return new PageImpl<>(members, pageable, total);
    }

    private JPAQuery<Member> dslFindSelectFromWhere(BooleanBuilder predicate) {
        return jpaQueryFactory.selectDistinct(member).from(member).where(predicate);
    }
}
