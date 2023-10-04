package com.sbs.demo5.base.security;

import com.sbs.demo5.domain.member.exception.EmailNotVerifiedAccessDeniedException;
import com.sbs.demo5.standard.util.Ut;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import java.io.IOException;

public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        if (accessDeniedException instanceof EmailNotVerifiedAccessDeniedException) {
            response.sendRedirect("/usr/member/notVerified?msg=" + Ut.url.encodeWithTtl(accessDeniedException.getMessage()));
            return;
        }

        super.handle(request, response, accessDeniedException);
    }
}
