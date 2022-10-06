package com.kmong.api.common.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ListResponse<T> extends Response{

    private List<T> datas;

    public ListResponse(String code, String message) {
        super(code, message);
    }

    @Builder
    public ListResponse(String code, String message, List<T> datas) {
        super(code, message);
        this.datas = datas;
    }
}
