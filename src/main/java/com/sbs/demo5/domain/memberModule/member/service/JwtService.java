package com.sbs.demo5.domain.memberModule.member.service;

import com.sbs.demo5.domain.memberModule.member.entity.Member;
import com.sbs.demo5.global.app.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class JwtService {

    public String genToken(Member actor, String keyType, long expireSeconds) {
        Claims claims = Jwts
                .claims()
                .setSubject(actor.getId() + "")
                .add("type", keyType)
                .build();

        Date now = new Date();
        Date validity = new Date(now.getTime() + 1000 * expireSeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, AppConfig.getJwtSecretKey())
                .compact();
    }

    public String genRefreshToken(Member actor) {
        return genToken(actor, "refresh", 60 * 60 * 24 * 30);
    }

    public String genAccessToken(Member actor) {
        return genToken(actor, "access", 60 * 10);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(AppConfig.getJwtSecretKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getMemberId(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(AppConfig.getJwtSecretKey()).build().parseClaimsJws(token).getPayload().getSubject());
    }
}
