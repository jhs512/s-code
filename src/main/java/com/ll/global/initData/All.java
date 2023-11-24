package com.ll.global.initData;

import com.ll.domain.baseModule.system.service.SystemService;
import com.ll.domain.articleModule.board.entity.Board;
import com.ll.domain.articleModule.board.service.BoardService;
import com.ll.domain.memberModule.member.entity.Member;
import com.ll.domain.memberModule.member.service.MemberService;
import com.ll.global.app.AppConfig;
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
    private SystemService systemService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private BoardService boardService;

    @Bean
    public ApplicationRunner initAll() {
        return args -> {
            if (systemService.isAllInitDataConfigured() == false) {
                self.work1();

                systemService.setAllInitDataConfigured(true);
            }
        };
    }

    @Transactional
    public void work1() {
        new File(AppConfig.getTempDirPath()).mkdirs();
        Member member1 = memberService.findByUsername("system")
                .orElseGet(() -> memberService.join("system", "", "시스템", "system@test.com", "").getData());

        Board board1 = boardService.findByCode("notice1")
                .orElseGet(() -> boardService.make("notice1", "공지사항", "<i class=\"fa-regular fa-flag\"></i>").getData());
        Board board2 = boardService.findByCode("free1")
                .orElseGet(() -> boardService.make("free1", "자유", "<i class=\"fa-solid fa-face-grin-tears\"></i>").getData());
    }
}