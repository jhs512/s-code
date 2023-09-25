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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final Rq rq;
    private final MemberService memberService;

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
        private MultipartFile profileImg;
    }

    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        System.out.println("joinForm : " + joinForm);
        RsData<Member> joinRs = memberService.join(joinForm.getUsername(), joinForm.getPassword(), joinForm.getNickname(), joinForm.getProfileImg());

        if (joinRs.isFail()) {
            return rq.historyBack(joinRs.getMsg());
        }

        return rq.redirect("/", joinRs.getMsg());
    }

    @GetMapping("/checkUsernameDup")
    @ResponseBody
    public RsData checkUsernameDup(String username) {
        return memberService.checkUsernameDup(username);
    }
}
