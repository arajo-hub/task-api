package com.kmong.api.common.exception;

import lombok.Getter;

@Getter
public class KmongNotFoundException extends KmongApiException {

    public KmongNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}