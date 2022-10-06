package com.kmong.api.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingleResponse<T> extends Response {

    private T data;

    @Builder
    public SingleResponse(String code, String message, T data) {
        super(code, message);
        this.data = data;
    }

}
