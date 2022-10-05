package com.kmong.api.member.exception;

import com.kmong.api.common.exception.KmongApiException;

public class MemberNotFoundException extends KmongApiException {

    private static final String MESSAGE = "존재하지 않는 회원입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}