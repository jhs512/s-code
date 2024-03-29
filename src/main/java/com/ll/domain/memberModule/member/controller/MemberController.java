package com.ll.domain.memberModule.member.controller;

import com.ll.domain.baseModule.base.exception.GlobalException;
import com.ll.domain.memberModule.member.entity.Member;
import com.ll.domain.memberModule.member.service.MemberService;
import com.ll.global.rq.Rq;
import com.ll.global.rsData.RsData;
import com.ll.standard.util.Ut;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
@Validated
public class MemberController {
    private final MemberService memberService;
    private final Rq rq;

    // 로그인 폼
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin() {
        return "domain/memberModule/member/login";
    }

    // 회원가입 폼
    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin() {
        return "domain/memberModule/member/join";
    }

    // 회원가입 처리
    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm) {
        RsData<Member> joinRs = memberService.join(
                joinForm.getUsername(),
                joinForm.getPassword(),
                joinForm.getNickname(),
                joinForm.getEmail(),
                joinForm.getProfileImg()
        );

        return rq.redirectOrBack("/member/login?lastUsername=" + joinForm.getUsername(), joinRs);
    }

    // 회원가입 단계, 아이디 중복 확인
    @PreAuthorize("isAnonymous()")
    @GetMapping("/checkUsernameDup")
    @ResponseBody
    public RsData<String> checkUsernameDup(
            @NotBlank @Length(min = 4) String username
    ) {
        return memberService.checkUsernameDup(username);
    }

    // 회원가입 단계, 이메일 중복 확인
    @PreAuthorize("isAnonymous()")
    @GetMapping("/checkEmailDup")
    @ResponseBody
    public RsData<String> checkEmailDup(
            @NotBlank @Length(min = 4) String email
    ) {
        email = email.replace(" ", "+");

        return memberService.checkEmailDup(email);
    }

    // 내 정보 수정, 이메일 중복 확인
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkCreatorNameDup")
    @ResponseBody
    public RsData<String> checkCreatorNameDup(
            @NotBlank @Length(min = 2) String creatorName
    ) {
        return memberService.checkCreatorNameDup(rq.getMember(), creatorName);
    }

    public boolean assertCheckPasswordAuthCodeVerified() {
        memberService
                .checkCheckPasswordAuthCode(rq.getMember(), rq.getParam("checkPasswordAuthCode", ""))
                .optional()
                .filter(RsData::isFail)
                .ifPresent((rsData) -> {
                    throw new GlobalException(rsData);
                });

        return true;
    }

    // 이 함수는 SecurityConfig 에서 사용된다.
    public boolean assertCurrentMemberVerified() {
        if (!memberService.isEmailVerified(rq.getMember()))
            throw new GlobalException("400", "이메일 인증 후 이용해주세요.");

        return true;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/notVerified")
    public String showNotVerified() {
        return "domain/memberModule/member/notVerified";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findUsername")
    public String showFindUsername() {
        return "domain/memberModule/member/findUsername";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String findUsername(
            @NotBlank @Length(min = 4) String email
    ) {
        return memberService.findByEmail(email)
                .map(member ->
                        rq.redirect(
                                "/member/login?lastUsername=%s".formatted(member.getUsername()),
                                "해당 회원의 아이디는 `%s` 입니다.".formatted(member.getUsername())
                        )
                )
                .orElseGet(() -> rq.historyBack("`%s` (은)는 존재하지 않은 회원 이메일 입니다.".formatted(email)));
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "domain/memberModule/member/findPassword";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(
            @NotBlank @Length(min = 4) String username,
            @NotBlank @Length(min = 4) String email
    ) {
        return memberService.findByUsernameAndEmail(username, email)
                .map(member -> {
                    memberService.sendTempPasswordToEmail(member);
                    return rq.redirect(
                            "/member/login?lastUsername=%s".formatted(member.getUsername()),
                            "해당 회원의 이메일로 임시 비밀번호를 발송하였습니다."
                    );
                }).orElseGet(() -> rq.historyBack("일치하는 회원이 존재하지 않습니다."));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public String showMe() {
        return "domain/memberModule/member/me";
    }

    @PreAuthorize("isAuthenticated() and @memberController.assertCheckPasswordAuthCodeVerified()")
    @GetMapping("/modify")
    public String showModify() {
        return "domain/memberModule/member/modify";
    }

    @PreAuthorize("isAuthenticated() and @memberController.assertCheckPasswordAuthCodeVerified()")
    @PostMapping("/modify")
    public String modify(@Valid ModifyForm modifyForm) {
        RsData<Member> modifyRs = memberService.modify(
                rq.getMember().getId(),
                modifyForm.getPassword(),
                modifyForm.getNickname(),
                modifyForm.getProfileImg()
        );

        return rq.redirectOrBack("/member/me", modifyRs);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/checkPassword")
    public String showCheckPassword(@NotBlank String redirectUrl) {
        if (rq.getMember().isSocialMember()) {
            String code = memberService.genCheckPasswordAuthCode(rq.getMember());

            redirectUrl = Ut.url.modifyQueryParam(redirectUrl, "checkPasswordAuthCode", code);

            return rq.redirect(redirectUrl);
        }

        return "domain/memberModule/member/checkPassword";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/checkPassword")
    public String checkPassword(
            @NotBlank @Length(min = 4) String password,
            @NotBlank String redirectUrl
    ) {
        if (!memberService.isSamePassword(rq.getMember(), password))
            return rq.historyBack("비밀번호가 일치하지 않습니다.");

        String code = memberService.genCheckPasswordAuthCode(rq.getMember());

        redirectUrl = Ut.url.modifyQueryParam(redirectUrl, "checkPasswordAuthCode", code);

        return rq.redirect(redirectUrl);
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class JoinForm {
        @NotBlank
        @Length(min = 4)
        private String username;
        @NotBlank
        @Length(min = 2)
        private String nickname;
        @NotBlank
        @Length(min = 4)
        private String password;
        @NotBlank
        @Length(min = 4)
        private String email;
        private MultipartFile profileImg;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class ModifyForm {
        @NotBlank
        @Length(min = 2)
        private String nickname;
        @Length(min = 4)
        private String password;
        private MultipartFile profileImg;
    }

    @PreAuthorize("isAuthenticated() and @memberController.assertCheckPasswordAuthCodeVerified()")
    @GetMapping("/beCreator")
    public String showbeCreator() {
        return "domain/memberModule/member/beCreator";
    }

    @SneakyThrows
    @PreAuthorize("isAuthenticated() and @memberController.assertCheckPasswordAuthCodeVerified()")
    @PostMapping("/beCreator")
    public String beCreator(@NotBlank @Length(min = 2) String creatorName) {
        Member member = rq.getMember();

        RsData<Member> rs = memberService.beCreator(member.getId(), creatorName);

        return rq.redirectOrBack("/member/me", rs);
    }
}
