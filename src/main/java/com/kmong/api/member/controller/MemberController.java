package com.kmong.api.member.controller;

import com.kmong.api.common.response.Response;
import com.kmong.api.member.exception.AlreadyLoginException;
import com.kmong.api.member.request.MemberCreate;
import com.kmong.api.member.request.MemberSearch;
import com.kmong.api.member.service.LoginService;
import com.kmong.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.kmong.api.common.constant.Constant.SESSION_ATTRIBUTE_MEMBER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;

    private final MemberService memberService;

    @PostMapping("/member/join")
    public ResponseEntity join(HttpSession session, @RequestBody @Valid MemberCreate memberCreate) {
        if (isAlreadyLoggedIn(session)) {
            throw new AlreadyLoginException("로그아웃 후 회원가입이 가능합니다.");
        }
        return memberService.join(memberCreate);
    }

    @PostMapping("/member/login")
    public ResponseEntity login(HttpSession session, @RequestBody @Valid MemberSearch memberSearch) {
        if (isAlreadyLoggedIn(session)) {
            throw new AlreadyLoginException("이미 로그인되어 있습니다.");
        }
        return loginService.login(session, memberSearch);
    }

    @PostMapping("/member/logout")
    public ResponseEntity logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity(new Response("200", "로그아웃에 성공했습니다."), HttpStatus.OK);
    }

    private boolean isAlreadyLoggedIn(HttpSession session) {
        return !ObjectUtils.isEmpty(session.getAttribute(SESSION_ATTRIBUTE_MEMBER_ID));
    }

}
