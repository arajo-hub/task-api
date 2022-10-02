package com.kmong.api.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.member.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.kmong.api.member.request.MemberSearch;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class MemberControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @BeforeEach
    @AfterEach
    void clearAll() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        Member member = Member.builder()
                                .id("test1234")
                                .email("test1234@naver.com")
                                .pwd("test1234")
                                .sessionId("sessionId").build();
        memberRepository.save(member);

        MemberSearch memberSearch = MemberSearch.builder().id(member.getId()).pwd(member.getPwd()).build();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSearch)))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("존재하지 않는 아이디로 로그인 시도")
    void loginNotExistsId() throws Exception {
        MemberSearch memberSearch = MemberSearch.builder().id("test1234").pwd("test1234").build();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSearch)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시도")
    void loginWrongPwd() throws Exception {
        Member member = Member.builder()
                .id("test1234")
                .email("test1234@naver.com")
                .pwd("test1234")
                .sessionId("sessionId").build();
        memberRepository.save(member);

        String wrongPwd = String.format("%s1", member.getPwd());
        MemberSearch memberSearch = MemberSearch.builder().id(member.getId()).pwd(wrongPwd).build();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberSearch)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그아웃")
    void logout() throws Exception {
        MockHttpSession session = new MockHttpSession(null, "test1234");
        mockMvc.perform(post("/logout")
                .session(session))
                .andExpect(status().isOk());
        assertTrue(session.isInvalid());
    }

}
