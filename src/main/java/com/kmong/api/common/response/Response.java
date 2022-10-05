package com.kmong.api.common.response;

import lombok.Getter;

@Getter
public class Response {

    private final String code;
    private final String message;

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

}