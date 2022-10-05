package com.kmong.api.order.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImpossibleProductView {

    private Long productId;
    private String productName;
    private int quantity;
    private int price;
    private boolean salesYn;
    private String reason;

    @Builder
    public ImpossibleProductView(Long productId, String productName, int quantity, int price, boolean salesYn, String reason) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.salesYn = salesYn;
        this.reason = reason;
    }
}
