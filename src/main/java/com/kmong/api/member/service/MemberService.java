package com.kmong.api.member.service;

import com.kmong.api.member.domain.Member;

import java.util.Optional;

public interface MemberService {

    /**
     * 아이디로 회원 검색
     * @param memberId 검색할 회원 아이디
     * @return 검색된 회원정보
     */
    Optional<Member> findById(String memberId);
}
