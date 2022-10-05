package com.kmong.api.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SingleResponse extends Response {

    private Object object;

    @Builder
    public SingleResponse(String code, String message, Object object) {
        super(code, message);
        this.object = object;
    }

}
