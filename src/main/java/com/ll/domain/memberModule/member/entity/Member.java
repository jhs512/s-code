package com.ll.domain.memberModule.member.entity;

import com.ll.global.jpa.baseEntity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
@ToString(callSuper = true)
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String creatorName;
    @Column(unique = true)
    private String email;

    public boolean isAdmin() {
        return "admin".equals(username);
    }

    public boolean isCreator() {
        return creatorName != null;
    }

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        // 모든 멤버는 admMember 권한을 가진다.
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));

        // username이 admin인 회원은 추가로 admin 권한도 가진다.
        if (isAdmin()) grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (isCreator()) grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CREATOR"));

        return grantedAuthorities;
    }

    public boolean isSocialMember() {
        return username.startsWith("KAKAO_");
    }

    public boolean isModifyAvailable() {
        return true;
    }

    public boolean isModifyPasswordAvailable() {
        return !isSocialMember();
    }

    public String getEmailForPrint() {
        if (isSocialMember()) return "-";
        return email;
    }

    public String getCreatorNameForPrint() {
        if (!isCreator()) return "-";
        return creatorName;
    }
}
