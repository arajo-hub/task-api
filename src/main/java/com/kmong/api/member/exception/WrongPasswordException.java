package com.kmong.api.member.exception;


import com.kmong.api.common.exception.KmongApiException;

public class WrongPasswordException extends KmongApiException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public WrongPasswordException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
