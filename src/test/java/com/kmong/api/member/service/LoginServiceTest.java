package com.kmong.api.member.service;

import com.kmong.api.config.encrypt.PwdEncryption;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.member.domain.Member;
import com.kmong.api.member.request.MemberSearch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class LoginServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LoginService loginService;

    @Test
    @DisplayName("로그인 성공")
    void login() {
        //given
        Member member = Member.builder()
                                .id("test1234")
                                .email("test1234@naver.com")
                                .pwd(PwdEncryption.encrypt("test1234"))
                                .sessionId("sessionId")
                                .build();
        memberRepository.save(member);

        //when
        MemberSearch memberSearch = MemberSearch.builder()
                                                .id("test1234")
                                                .pwd("test1234")
                                                .build();
        ResponseEntity response = loginService.login(new MockHttpSession(), memberSearch);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 로그인 시도")
    void loginNotExistsId() {
        MemberSearch memberSearch = MemberSearch.builder()
                                                .id("test1234")
                                                .pwd("test1234")
                                                .build();
        ResponseEntity response = loginService.login(new MockHttpSession(), memberSearch);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도")
    void loginWrongPwd() {
        //given
        Member member = Member.builder()
                                .id("test1234")
                                .email("test1234@naver.com")
                                .pwd("test1234")
                                .sessionId("sessionId")
                                .build();
        memberRepository.save(member);

        //when
        String wrongPwd = String.format("%s1", member.getPwd());
        MemberSearch memberSearch = MemberSearch.builder()
                                                .id("test1234")
                                                .pwd(wrongPwd)
                                                .build();
        ResponseEntity response = loginService.login(new MockHttpSession(), memberSearch);

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
