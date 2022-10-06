package com.kmong.api.member.exception;

import com.kmong.api.common.exception.KmongApiException;

public class DuplicateMemberException extends KmongApiException {

    private static final String MESSAGE = "중복된 아이디 혹은 중복된 이메일의 회원이 존재합니다.";

    public DuplicateMemberException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 409;
    }

}
