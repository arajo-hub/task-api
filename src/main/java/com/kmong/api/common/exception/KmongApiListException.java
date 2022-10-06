package com.kmong.api.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public abstract class KmongApiListException extends KmongApiException {

    private List list;

    public KmongApiListException(String message, List list) {
        super(message);
        this.list = list;
    }

    public KmongApiListException(String message, Throwable cause, List list) {
        super(message, cause);
        this.list = list;
    }

    public KmongApiListException(String message) {
        super(message);
    }

    public abstract int getStatusCode();

}
