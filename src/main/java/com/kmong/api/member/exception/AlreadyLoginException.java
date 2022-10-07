package com.kmong.api.member.exception;

import com.kmong.api.common.exception.KmongApiException;

public class AlreadyLoginException extends KmongApiException {

    public AlreadyLoginException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
