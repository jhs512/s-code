package com.sbs.demo5.domain.member.controller;

import com.sbs.demo5.base.rsData.RsData;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.member.service.MemberService;
import com.sbs.demo5.standard.util.Ut;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    public String showJoin() {
        return "usr/member/join";
    }

    @Getter
    @AllArgsConstructor
    public static class JoinForm {
        @NotBlank
        private String username;
        @NotBlank
        private String nickname;
        @NotBlank
        private String password;
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getUsername());

        if (joinRs.isFail()) {
            return "redirect:/usr/member/join?failMsg=" + Ut.url.encode(joinRs.getMsg());
        }

        return "redirect:/?msg=" + Ut.url.encode(joinRs.getMsg());
    }
}
