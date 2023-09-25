package com.sbs.demo5.domain.member.controller;

import com.sbs.demo5.base.rsData.RsData;
import com.sbs.demo5.base.rq.Rq;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.member.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "usr/member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "usr/member/join";
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class JoinForm {
        @NotBlank
        private String username;
        @NotBlank
        private String nickname;
        @NotBlank
        private String password;
        @NotBlank
        private String email;
        private MultipartFile profileImg;
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        System.out.println("joinForm : " + joinForm);
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getNickname(), joinForm.getEmail(), joinForm.getProfileImg());

        if (joinRs.isFail()) {
            return rq.historyBack(joinRs.getMsg());
        }

        return rq.redirect("/", joinRs.getMsg());
    }

    @GetMapping("/checkUsernameDup")
    @ResponseBody
    public RsData<String> checkUsernameDup(String username) {
        return memberService.checkUsernameDup(username);
    }

    @GetMapping("/checkEmailDup")
    @ResponseBody
    public RsData<String> checkEmailDup(String email) {
        return memberService.checkEmailDup(email);
    }
}
