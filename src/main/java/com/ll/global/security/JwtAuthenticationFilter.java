package com.ll.global.security;

import com.ll.domain.memberModule.member.entity.Member;
import com.ll.domain.memberModule.member.service.MemberService;
import com.ll.global.rq.Rq;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Rq rq;
    private final MemberService memberService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String refreshToken = rq.getCookieValue("refreshToken", "");
        String accessToken = rq.getCookieValue("accessToken", "");

        if (!memberService.validateToken(accessToken)) {
            if (!memberService.validateToken(refreshToken)) {
                filterChain.doFilter(request, response);
                return;
            }

            Member member = memberService.getMemberByToken(refreshToken);

            accessToken = memberService.genAccessToken(member);
            rq.addCrossDomainCookie("accessToken", accessToken);
        }

        Member member = memberService.getMemberByToken(accessToken);

        rq.setLogined(member);

        filterChain.doFilter(request, response);
    }
}
