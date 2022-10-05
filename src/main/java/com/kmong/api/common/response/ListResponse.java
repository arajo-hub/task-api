package com.kmong.api.common.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ListResponse<T> extends Response{

    private List<T> objects;

    public ListResponse(String code, String message) {
        super(code, message);
    }

    @Builder
    public ListResponse(String code, String message, List<T> objects) {
        super(code, message);
        this.objects = objects;
    }
}
