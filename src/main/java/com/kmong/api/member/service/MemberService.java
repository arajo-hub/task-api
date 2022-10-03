package com.kmong.api.member.service;

import com.kmong.api.member.domain.Member;
import com.kmong.api.member.request.MemberCreate;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface MemberService {

    /**
     * 아이디로 회원 검색
     * @param memberId 검색할 회원 아이디
     * @return 검색된 회원정보
     */
    Optional<Member> findById(String memberId);

    /**
     * 회원가입
     * @param memberCreate 가입할 회원정보
     * @return 회원가입 결과
     */
    ResponseEntity join(MemberCreate memberCreate);
}
