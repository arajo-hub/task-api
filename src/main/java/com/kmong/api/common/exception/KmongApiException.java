package com.kmong.api.common.exception;

import lombok.Getter;

@Getter
public abstract class KmongApiException extends RuntimeException {

    public KmongApiException(String message) {
        super(message);
    }

    public KmongApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

}
