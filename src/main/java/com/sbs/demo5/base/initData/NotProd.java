package com.sbs.demo5.base.initData;

import com.sbs.demo5.base.app.AppConfig;
import com.sbs.demo5.domain.article.entity.Article;
import com.sbs.demo5.domain.article.service.ArticleService;
import com.sbs.demo5.domain.board.entity.Board;
import com.sbs.demo5.domain.board.service.BoardService;
import com.sbs.demo5.domain.member.entity.Member;
import com.sbs.demo5.domain.member.service.MemberService;
import com.sbs.demo5.domain.post.entity.Post;
import com.sbs.demo5.domain.post.service.PostService;
import com.sbs.demo5.standard.util.Ut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Profile("!prod")
public class NotProd {
    @Value("${custom.security.oauth2.client.registration.kakao.devUserOauthId}")
    private String kakaoDevUserOAuthId;

    @Autowired
    @Lazy
    private NotProd self;

    @Autowired
    private BoardService boardService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private PostService postService;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            self.work1();
            self.work2();
        };
    }

    @Transactional
    public void work1() {
        Board board1 = boardService.make("notice1", "공지사항", "<i class=\"fa-regular fa-flag\"></i>").getData();
        Board board2 = boardService.make("free1", "자유", "<i class=\"fa-solid fa-face-grin-tears\"></i>").getData();

        Member member1 = memberService.join("admin", "1234", "admin", "admin@test.com", "").getData();
        Member member2 = memberService.join("user1", "1234", "nickname1", "user1@test.com", "").getData();
        Member member3 = memberService.join("user2", "1234", "nickname2", "user2@test.com", "").getData();
        Member member4 = memberService.join("user3", "1234", "nickname3", "user3@test.com", "").getData();

        memberService.setEmailVerified(member1);
        memberService.setEmailVerified(member2);
        memberService.setEmailVerified(member3);

        Member memberByKakao = memberService.whenSocialLogin("KAKAO", "KAKAO__%s".formatted(kakaoDevUserOAuthId), "홍길동", "");

        Article article1 = articleService.write(board1, member1, "제목 1", "#자바 #HTML", "내용 1").getData();
        Article article2 = articleService.write(board1, member2, "제목 2", "#CSS #HTML #홍길동", "내용 2").getData();
        Article article3 = articleService.write(board2, member1, "제목 3", "#자바의 정석", "내용 3").getData();
        Article article4 = articleService.write(board2, member2, "제목 4", "#홍길동", "내용 4").getData();

        String file1Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/common/common.css");
        String file2Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/common/common.js");
        articleService.saveAttachmentFile(article1, file1Path, 1L);
        articleService.saveAttachmentFile(article1, file2Path, 2L);

        Post post1 = postService.write(member1, "제목 1", "#자바 #HTML", "내용 1", true).getData();

        String file3Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/common/common.css");
        String file4Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/common/common.js");
        postService.saveAttachmentFile(post1, file3Path, 1L);
        postService.saveAttachmentFile(post1, file4Path, 2L);

        Post post2 = postService.write(member1, "제목 2", "#CSS #HTML #Python", "내용 2", true).getData();
        Post post3 = postService.write(member1, "제목 3", "#Java #HTML", "내용 3", true).getData();
        Post post4 = postService.write(member2, "제목 4", "#Python #Script", "내용 4", false).getData();
        Post post5 = postService.write(member2, "제목 5", "#Java #JSP", "내용 5", false).getData();
        Post post6 = postService.write(member2, "제목 6", "#CSS #Hungry #Python", "내용 6", true).getData();
    }

    @Transactional
    public void work2() {
        Article article1 = articleService.findById(1L).get();
        articleService.modify(article1, "제목 1 수정", "#자바2 #HTML", "내용 1\n수정", "내용 1<br />수정");
    }
}
