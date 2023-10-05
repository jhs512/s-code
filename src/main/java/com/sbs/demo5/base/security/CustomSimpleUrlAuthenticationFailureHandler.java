package com.sbs.demo5.base.security;

import com.sbs.demo5.standard.util.Ut;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // request 객체에서 폼에서 POST 방식으로 username 을 이름으로 하여 보낸 값을 얻고 싶어
        String username = request.getParameter("username");

        setDefaultFailureUrl("/usr/member/login?username=" + username + "&failMsg=" + Ut.url.encodeWithTtl("올바르지 않은 회원정보 입니다."));
        super.onAuthenticationFailure(request, response, exception);
    }
}
