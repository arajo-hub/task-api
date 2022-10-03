package com.kmong.api.member.controller;

import com.kmong.api.member.request.MemberCreate;
import com.kmong.api.member.request.MemberSearch;
import com.kmong.api.member.service.LoginService;
import com.kmong.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final LoginService loginService;

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody MemberCreate memberCreate) {
        return memberService.join(memberCreate);
    }

    @PostMapping("/login")
    public ResponseEntity login(HttpSession session, @RequestBody MemberSearch memberSearch) {
        return loginService.login(session, memberSearch);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpSession session) {
        session.invalidate();
        return new ResponseEntity(HttpStatus.OK);
    }

}
