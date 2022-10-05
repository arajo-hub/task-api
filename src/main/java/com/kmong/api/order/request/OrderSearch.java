package com.kmong.api.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String memberId;

    @Builder
    public OrderSearch(String memberId) {
        this.memberId = memberId;
    }

}
