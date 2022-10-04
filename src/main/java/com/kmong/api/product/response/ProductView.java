package com.kmong.api.product.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductView {

    private Long id;
    private String productName;
    private int quantity;
    private int price;
    private boolean salesYn;

    @Builder
    public ProductView(Long id, String productName, int quantity, int price, boolean salesYn) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.salesYn = salesYn;
    }
}
