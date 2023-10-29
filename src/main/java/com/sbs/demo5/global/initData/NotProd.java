package com.sbs.demo5.global.initData;

import com.sbs.demo5.domain.baseModule.system.service.SystemService;
import com.sbs.demo5.domain.boardModule.article.entity.Article;
import com.sbs.demo5.domain.boardModule.article.service.ArticleService;
import com.sbs.demo5.domain.boardModule.board.entity.Board;
import com.sbs.demo5.domain.boardModule.board.service.BoardService;
import com.sbs.demo5.domain.bookModule.book.service.BookService;
import com.sbs.demo5.domain.memberModule.member.entity.Member;
import com.sbs.demo5.domain.memberModule.member.service.MemberService;
import com.sbs.demo5.domain.postModule.post.entity.Post;
import com.sbs.demo5.domain.postModule.post.service.PostService;
import com.sbs.demo5.domain.postModule.postKeyword.entity.PostKeyword;
import com.sbs.demo5.global.app.AppConfig;
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
    @Autowired
    private BookService bookService;
    @Autowired
    private SystemService systemService;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {

            if (systemService.isNotProdInitDataConfigured() == false) {
                self.work1();
                self.work2();

                systemService.setNotProdInitDataConfigured(true);
            }
        };
    }

    @Transactional
    public void work1() {
        Board board1 = boardService.findByCode("notice1").get();
        Board board2 = boardService.findByCode("free1").get();

        Member member2 = memberService.join("admin", "1234", "admin", "admin@test.com", "").getData();
        Member member3 = memberService.join("user1", "1234", "nickname1", "user1@test.com", "").getData();
        Member member4 = memberService.join("user2", "1234", "nickname2", "user2@test.com", "").getData();
        Member member5 = memberService.join("user3", "1234", "nickname3", "user3@test.com", "").getData();

        memberService.setEmailVerified(member2);
        memberService.setEmailVerified(member3);
        memberService.setEmailVerified(member4);

        memberService.beProducer(member2.getId(), "장필우");
        memberService.beProducer(member3.getId(), "고니");

        Member memberByKakao = memberService.whenSocialLogin("KAKAO", "KAKAO__%s".formatted(kakaoDevUserOAuthId), "홍길동", "");

        Article article1 = articleService.write(board1, member2, "제목 1", "#자바 #HTML", "내용 1").getData();
        Article article2 = articleService.write(board1, member3, "제목 2", "#CSS #HTML #홍길동", "내용 2").getData();
        Article article3 = articleService.write(board2, member2, "제목 3", "#자바의 정석", "내용 3").getData();
        Article article4 = articleService.write(board2, member3, "제목 4", "#홍길동", "내용 4").getData();

        String file1Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/globalModule/globalModule.css");
        String file2Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/globalModule/globalModule.js");
        articleService.saveAttachmentFile(article1, file1Path, 1L);
        articleService.saveAttachmentFile(article1, file2Path, 2L);

        Post post1 = postService.write(member2, "제목 1", "#자바 #HTML", "내용 1", true).getData();

        PostKeyword postKeywordHtml = post1.getPostTags()
                .stream()
                .filter(postTag -> postTag.getContent().equals("HTML"))
                .map(postTag -> postTag.getPostKeyword())
                .findFirst()
                .get();

        String file3Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/globalModule/globalModule.css");
        String file4Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/globalModule/globalModule.js");
        postService.saveAttachmentFile(post1, file3Path, 1L);
        postService.saveAttachmentFile(post1, file4Path, 2L);

        Post post2 = postService.write(member2, "제목 2", "#CSS #HTML #Python", "내용 2", true).getData();
        Post post3 = postService.write(member2, "제목 3", "#Java #HTML", "내용 3", true).getData();
        Post post4 = postService.write(member3, "제목 4", "#Python #Script", "내용 4", false).getData();
        Post post5 = postService.write(member3, "제목 5", "#Java #JSP", "내용 5", false).getData();
        Post post6 = postService.write(member3, "제목 6", "#CSS #Hungry #Python", "내용 6", true).getData();

        bookService.write(member2, postKeywordHtml, "HTML 기초", "#HTML #프론트 엔드", "내용 1", Ut.markdown.toHtml("내용 1"), true);
    }

    @Transactional
    public void work2() {
        Article article1 = articleService.findById(1L).get();
        articleService.modify(article1, "제목 1 수정", "#자바2 #HTML", "내용 1\n수정", "내용 1<br />수정");
    }
}
