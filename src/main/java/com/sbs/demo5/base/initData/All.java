package com.sbs.demo5.base.initData;

import com.sbs.demo5.base.app.AppConfig;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Configuration
public class All {
    @Autowired
    @Lazy
    private All self;

    @Autowired
    private MemberService memberService;

    @Bean
    public ApplicationRunner initAll() {
        return args -> {
            self.work1();
        };
    }

    @Transactional
    public void work1() {
        new File(AppConfig.getTempDirPath()).mkdirs();
        Member member1 = memberService.join("system", "1234", "Tltmxpa2313", "system@system.com", "").getData();
    }
}