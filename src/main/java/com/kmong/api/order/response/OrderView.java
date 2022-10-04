package com.kmong.api.order.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderView {

    private Long id;
    private List<OrderProductView> orderProductViews;
    private LocalDateTime orderDatetime;
    private int totalPrice;

    @Builder
    public OrderView(Long id, List<OrderProductView> orderProductViews, LocalDateTime orderDatetime, int totalPrice) {
        this.id = id;
        this.orderProductViews = orderProductViews;
        this.orderDatetime = orderDatetime;
        this.totalPrice = totalPrice;
    }
}
