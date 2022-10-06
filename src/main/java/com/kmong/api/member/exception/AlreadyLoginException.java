package com.kmong.api.member.exception;

import com.kmong.api.common.exception.KmongApiException;

public class AlreadyLoginException extends KmongApiException {

    private static final String MESSAGE = "로그아웃 후 회원가입이 가능합니다.";

    public AlreadyLoginException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
