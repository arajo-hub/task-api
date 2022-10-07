package com.kmong.api.member.service;

import com.kmong.api.config.encrypt.PwdEncryption;
import com.kmong.api.member.domain.Member;
import com.kmong.api.member.exception.MemberNotFoundException;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.member.request.MemberSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                                .pwd(PwdEncryption.encrypt("Test@1234"))
                                .build();
        memberRepository.save(member);

        //when
        MemberSearch memberSearch = MemberSearch.builder()
                                                .memberId("test1234")
                                                .pwd("Test@1234")
                                                .build();
        ResponseEntity response = loginService.login(new MockHttpSession(), memberSearch);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 로그인 시도")
    void loginNotExistsId() {
        assertThrows(MemberNotFoundException.class, () -> {
            MemberSearch memberSearch = MemberSearch.builder()
                    .memberId("test1234")
                    .pwd("Test@1234")
                    .build();
            loginService.login(new MockHttpSession(), memberSearch);
        });
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도")
    void loginWrongPwd() {
        assertThrows(RuntimeException.class, () -> {
            Member member = Member.builder()
                    .id("test1234")
                    .email("test1234@naver.com")
                    .pwd("Test@1234")
                    .build();
            memberRepository.save(member);

            //when
            String wrongPwd = String.format("%s1", member.getPwd());
            MemberSearch memberSearch = MemberSearch.builder()
                    .memberId("test1234")
                    .pwd(wrongPwd)
                    .build();
            loginService.login(new MockHttpSession(), memberSearch);
        });
    }

}
