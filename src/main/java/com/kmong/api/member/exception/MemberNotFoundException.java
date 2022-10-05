package com.kmong.api.member.exception;

import com.kmong.api.common.exception.KmongNotFoundException;

public class MemberNotFoundException extends KmongNotFoundException {

    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

}