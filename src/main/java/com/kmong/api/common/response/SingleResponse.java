package com.kmong.api.common.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SingleResponse<T> extends Response {

    private T object;

    @Builder
    public SingleResponse(String code, String message, T object) {
        super(code, message);
        this.object = object;
    }

}
