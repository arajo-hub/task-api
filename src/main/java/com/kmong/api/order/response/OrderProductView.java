package com.kmong.api.order.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductView {

    private Long id;
    private String orderProductName;
    private int quantity;
    private int price;

    @Builder
    public OrderProductView(Long id, String orderProductName, int quantity, int price) {
        this.id = id;
        this.orderProductName = orderProductName;
        this.quantity = quantity;
        this.price = price;
    }
}
