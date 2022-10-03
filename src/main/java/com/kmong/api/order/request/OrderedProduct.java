package com.kmong.api.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderedProduct {

    private Long id;
    private int quantity;

    @Builder
    public OrderedProduct(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
