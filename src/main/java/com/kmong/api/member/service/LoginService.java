package com.kmong.api.member.service;

import com.kmong.api.member.request.MemberSearch;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

public interface LoginService {

    /**
     * 로그인
     * @param httpSession 로그인 시도하는 세션
     * @param memberSearch 로그인할 회원정보
     * @return 로그인결과
     */
    ResponseEntity login(HttpSession httpSession, MemberSearch memberSearch);

}
