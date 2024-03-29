package com.ll.global.initData;

import com.ll.domain.articleModule.article.entity.Article;
import com.ll.domain.articleModule.article.service.ArticleService;
import com.ll.domain.articleModule.board.entity.Board;
import com.ll.domain.articleModule.board.service.BoardService;
import com.ll.domain.baseModule.system.service.SystemService;
import com.ll.domain.bookModule.book.service.BookService;
import com.ll.domain.memberModule.member.entity.Member;
import com.ll.domain.memberModule.member.service.MemberService;
import com.ll.domain.postModule.post.entity.Post;
import com.ll.domain.postModule.post.service.PostService;
import com.ll.domain.postModule.postKeyword.entity.PostKeyword;
import com.ll.global.app.AppConfig;
import com.ll.standard.util.Ut;
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
    @Autowired
    @Lazy
    private NotProd self;

    @Autowired
    private SystemService systemService;
    @Autowired
    private MemberService memberService;

    @Autowired
    private BoardService boardService;
    @Autowired
    private ArticleService articleService;

    @Autowired
    private PostService postService;
    @Autowired
    private BookService bookService;

    @Value("${custom.security.oauth2.client.registration.kakao.devUser1OauthId}")
    private String kakaoDevUser1OAuthId;
    @Value("${custom.security.oauth2.client.registration.kakao.devUser2OauthId}")
    private String kakaoDevUser2OAuthId;
    @Value("${custom.security.oauth2.client.registration.kakao.devUser3OauthId}")
    private String kakaoDevUser3OAuthId;
    @Value("${custom.security.oauth2.client.registration.kakao.devUser4OauthId}")
    private String kakaoDevUser4OAuthId;

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

        memberService.beCreator(member2.getId(), "장필우");
        memberService.beCreator(member3.getId(), "고니");

        if (Ut.str.hasLength(kakaoDevUser1OAuthId))
            memberService.whenSocialLogin("KAKAO", "KAKAO__%s".formatted(kakaoDevUser1OAuthId), "팀원1", "");

        if (Ut.str.hasLength(kakaoDevUser2OAuthId))
            memberService.whenSocialLogin("KAKAO", "KAKAO__%s".formatted(kakaoDevUser2OAuthId), "팀원2", "");

        if (Ut.str.hasLength(kakaoDevUser3OAuthId))
            memberService.whenSocialLogin("KAKAO", "KAKAO__%s".formatted(kakaoDevUser3OAuthId), "팀원3", "");

        if (Ut.str.hasLength(kakaoDevUser4OAuthId))
            memberService.whenSocialLogin("KAKAO", "KAKAO__%s".formatted(kakaoDevUser4OAuthId), "팀원4", "");

        Article article1 = articleService.write(board1, member2, "제목 1", "#자바 #HTML", "내용 1").getData();
        Article article2 = articleService.write(board1, member3, "제목 2", "#CSS #HTML #홍길동", "내용 2").getData();
        Article article3 = articleService.write(board2, member2, "제목 3", "#자바의 정석", "내용 3").getData();
        Article article4 = articleService.write(board2, member3, "제목 4", "#홍길동", "내용 4").getData();

        String file1Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/global/global.css");
        String file2Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/global/global.js");
        articleService.saveAttachmentFile(article1, file1Path, 1L);
        articleService.saveAttachmentFile(article1, file2Path, 2L);

        Post post1 = postService.write(member2, "제목 1", "#자바 #HTML", "내용 1", true).getData();

        PostKeyword postKeywordHtml = post1.getPostTags()
                .stream()
                .filter(postTag -> postTag.getContent().equals("HTML"))
                .map(postTag -> postTag.getPostKeyword())
                .findFirst()
                .get();

        String file3Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/global/global.css");
        String file4Path = Ut.file.tempCopy(AppConfig.getResourcesStaticDirPath() + "/resource/global/global.js");
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
