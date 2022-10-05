package com.kmong.api.member.service.impl;

import com.kmong.api.config.encrypt.PwdEncryption;
import com.kmong.api.member.domain.Member;
import com.kmong.api.member.exception.MemberNotFoundException;
import com.kmong.api.member.exception.WrongPasswordException;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.member.request.MemberSearch;
import com.kmong.api.member.response.MemberView;
import com.kmong.api.member.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;

    /**
     * 로그인
     * @param httpSession 로그인 시도하는 세션
     * @param memberSearch 로그인할 회원정보
     * @return 로그인결과
     */
    @Override
    public ResponseEntity<MemberView> login(HttpSession httpSession, MemberSearch memberSearch) {
        ResponseEntity response = new ResponseEntity(HttpStatus.OK);
        Optional<Member> memberFromDB = memberRepository.findById(memberSearch.getId());
        if (memberFromDB.isPresent()) {
            Member memberFindById = memberFromDB.get();
            if (memberFindById.getPwd().equals(PwdEncryption.encrypt(memberSearch.getPwd()))) {
                httpSession.setAttribute("id", memberFindById.getId());
                response = new ResponseEntity(memberFindById.toMemberView(), HttpStatus.OK);
            } else {
                throw new WrongPasswordException();
            }
        } else {
            throw new MemberNotFoundException();
        }
        return response;
    }

}
