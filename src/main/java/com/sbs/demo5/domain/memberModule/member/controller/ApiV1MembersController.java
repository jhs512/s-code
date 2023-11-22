package com.sbs.demo5.domain.memberModule.member.controller;

import com.sbs.demo5.domain.baseModule.base.exception.HttpException;
import com.sbs.demo5.domain.memberModule.member.dto.MemberDto;
import com.sbs.demo5.domain.memberModule.member.entity.Member;
import com.sbs.demo5.domain.memberModule.member.service.MemberService;
import com.sbs.demo5.global.rq.Rq;
import com.sbs.demo5.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Validated
public class ApiV1MembersController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final Rq rq;

    @Getter
    @Setter
    public static class LoginRequestBody {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    public static class LoginResponseBody extends MeResponseBody {
        public LoginResponseBody(Member member) {
            super(member);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public RsData<LoginResponseBody> login(@Valid @RequestBody LoginRequestBody loginDto) {
        Member member = memberService.findByUsername(loginDto.getUsername()).get();

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new HttpException(403, "F-1", "비밀번호가 일치하지 않습니다.");
        }

        String refreshToken = memberService.genRefreshToken(member);
        String accessToken = memberService.genAccessToken(member);

        rq.addCrossDomainCookie("refreshToken", refreshToken);
        rq.addCrossDomainCookie("accessToken", accessToken);

        return RsData.of("S-1", "로그인 성공", new LoginResponseBody(member));
    }

    @Getter
    public static class MeResponseBody {
        private final MemberDto item;

        public MeResponseBody(Member member) {
            this.item = new MemberDto(member);
        }
    }

    @GetMapping("/me")
    @ResponseBody
    public RsData<MeResponseBody> getMe() {
        Member member = rq.getMember();

        return RsData.of("S-1", "성공", new MeResponseBody(member));
    }

    @PostMapping("/logout")
    @ResponseBody
    public RsData<LoginResponseBody> logout() {
        rq.removeCookie("refreshToken");
        rq.removeCookie("accessToken");

        return RsData.of("S-1", "로그아웃 성공");
    }
}
