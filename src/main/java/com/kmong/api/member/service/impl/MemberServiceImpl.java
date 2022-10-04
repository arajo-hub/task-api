package com.kmong.api.member.service.impl;

import com.kmong.api.member.domain.Member;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.member.request.MemberCreate;
import com.kmong.api.member.response.MemberView;
import com.kmong.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Optional<Member> findById(String memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public ResponseEntity<MemberView> join(MemberCreate memberCreate) {
        ResponseEntity response = new ResponseEntity(HttpStatus.OK);
        Member member = memberCreate.toMember();
        Optional<Member> savedResult = memberRepository.save(member);
        if (savedResult.isPresent()) {
            MemberView memberView = savedResult.get().toMemberView();
            response = new ResponseEntity(memberView, HttpStatus.OK);
        } else {
            response = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(memberRepository.save(member), HttpStatus.OK);
    }
}
