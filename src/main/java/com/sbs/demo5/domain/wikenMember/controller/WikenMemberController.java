package com.sbs.demo5.domain.wikenMember.controller;

import com.sbs.demo5.base.rq.Rq;
import com.sbs.demo5.base.rsData.RsData;
import com.sbs.demo5.domain.base.exception.NeedHistoryBackException;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.member.service.MemberService;
import com.sbs.demo5.domain.post.service.PostService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/wikenMember")
@RequiredArgsConstructor
@Validated
public class WikenMemberController {
    private final MemberService memberService;
    private final PostService postService;
    private final Rq rq;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "usr/wikenMember/login";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/login")
    @Transactional
    public String login(
            @NotBlank String username
    ) {
        memberService.findByUsername(username)
                .ifPresent(member -> {
                    throw new NeedHistoryBackException("마이그레이션 완료된 회원입니다. 정상적인 로그인을 진행해주세요.");
                });

        Member member = memberService.join(
                username,
                "1234",
                username,
                username + "@test.com",
                ""
        ).getData();

        memberService.setEmailVerified(member);

        postService.findByAddi2(member.getUsername())
                .stream()
                .forEach(post -> {
                    if (post.getAuthor().getUsername().equals("garage")) post.setAuthor(member);
                });

        return rq.redirect("/usr/member/login?username=" + username, "마이그레이션이 완료되었습니다. 로그인을 진행해주세요.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/itsMine/{id}")
    @Transactional
    public RsData<?> itsMine(
            @PathVariable @NotNull Long id
    ) {
        postService.findById(id)
                .ifPresent(post -> {
                    if (post.getAuthor().getUsername().equals("garage")) post.setAuthor(rq.getMember());
                });

        return RsData.of("S-1", "마이그레이션이 완료되었습니다.");
    }
}
