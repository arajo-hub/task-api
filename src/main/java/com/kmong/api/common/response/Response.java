package com.kmong.api.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response {

    private String code;
    private String message;

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

}