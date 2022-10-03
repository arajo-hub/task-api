package com.kmong.api.member.service;

import com.kmong.api.member.domain.Member;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.member.request.MemberCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입")
    void join() {
        MemberCreate memberCreate = MemberCreate.builder()
                                                .id("test1234")
                                                .email("test1234@kmong.co.kr")
                                                .pwd("test1234")
                                                .build();
        ResponseEntity response = memberService.join(memberCreate);

        Optional<Member> memberFindById = memberService.findById(memberCreate.getId());
        Member member = memberFindById.isPresent() ? memberFindById.get() : null;

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(memberCreate.getId(), member.getId());
        assertEquals(memberCreate.getEmail(), member.getEmail());
        assertEquals(memberCreate.getEncryptedPwd(), member.getPwd());
    }

}
