package com.ll.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ApplicationContext context;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        authorizeHttpRequests -> authorizeHttpRequests
                                .requestMatchers(
                                        PathRequest.toStaticResources().atCommonLocations(),
                                        PathRequest.toH2Console()
                                )
                                .permitAll()
                                .requestMatchers(
                                        "/resource/**", "/gen/**"
                                )
                                .permitAll()
                                .requestMatchers("/member/notVerified")
                                .permitAll()
                                .requestMatchers(
                                        "/**"
                                )
                                .access(accessOf("isAnonymous() or @memberController.assertCurrentMemberVerified()"))
                                .requestMatchers(
                                        "/adm/**"
                                )
                                .hasRole("ADMIN")
                                .anyRequest()
                                .permitAll()
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .oauth2Login(
                        oauth2Login -> oauth2Login
                                .loginPage("/member/login")
                )
                .headers(
                        headers -> headers
                                .addHeaderWriter(
                                        new XFrameOptionsHeaderWriter(
                                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)
                                )
                )
                .csrf(
                        csrf -> csrf
                                .ignoringRequestMatchers("/post/modifyBody/**", "/h2-console/**")
                                .ignoringRequestMatchers(
                                        PathRequest.toH2Console() + "/**"
                                )

                )
                .headers(
                        headers -> headers
                                .addHeaderWriter(
                                        new XFrameOptionsHeaderWriter(
                                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                                        )
                                )
                )
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/member/login")
                                .successHandler(new CustomSimpleUrlAuthenticationSuccessHandler())
                                .failureHandler(new CustomSimpleUrlAuthenticationFailureHandler())
                )
                .logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                )
        ;
        return http.build();
    }

    private WebExpressionAuthorizationManager accessOf(String expressionString) {
        DefaultHttpSecurityExpressionHandler expressionHandler = new DefaultHttpSecurityExpressionHandler();
        expressionHandler.setApplicationContext(context);
        WebExpressionAuthorizationManager authorization = new WebExpressionAuthorizationManager(expressionString);
        authorization.setExpressionHandler(expressionHandler);

        return authorization;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
