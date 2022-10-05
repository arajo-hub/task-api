package com.kmong.api.member.service.impl;

import com.kmong.api.common.response.SingleResponse;
import com.kmong.api.member.domain.Member;
import com.kmong.api.member.exception.DuplicateMemberException;
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
        ResponseEntity response = new ResponseEntity(HttpStatus.CREATED);
        if (memberRepository.isAlreadyExist(memberCreate)) {
            throw new DuplicateMemberException();
        } else {
            Member member = memberCreate.toMember();
            memberRepository.save(member);
            MemberView memberView = member.toMemberView();
            SingleResponse body = SingleResponse.builder()
                                                .code("201")
                                                .message("회원가입되었습니다.")
                                                .object(member.toMemberView())
                                                .build();
            response = new ResponseEntity(body, HttpStatus.CREATED);
        }

        return response;
    }
}
