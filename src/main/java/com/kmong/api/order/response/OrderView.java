package com.kmong.api.order.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderView {

    private Long orderId;
    private List<OrderProductView> orderProductViews;
    private LocalDateTime orderDatetime;
    private int totalPrice;

    @Builder
    public OrderView(Long orderId, List<OrderProductView> orderProductViews, LocalDateTime orderDatetime, int totalPrice) {
        this.orderId = orderId;
        this.orderProductViews = orderProductViews;
        this.orderDatetime = orderDatetime;
        this.totalPrice = totalPrice;
    }
}
