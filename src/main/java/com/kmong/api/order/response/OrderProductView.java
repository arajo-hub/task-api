package com.kmong.api.order.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductView {

    private Long productId;
    private String productName;
    private int quantity;
    private int price;

    @Builder
    public OrderProductView(Long productId, String productName, int quantity, int price) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}
