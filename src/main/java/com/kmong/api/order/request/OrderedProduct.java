package com.kmong.api.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
@Setter
public class OrderedProduct {

    @Positive(message = "주문하고자 하는 상품아이디를 정확하게 입력해주세요.")
    private Long productId;
    @Positive(message = "수량은 양수만 입력 가능합니다.")
    private int quantity;

    @Builder
    public OrderedProduct(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

}
