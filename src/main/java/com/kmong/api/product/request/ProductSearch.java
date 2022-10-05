package com.kmong.api.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearch {

    private Long productId;

    private String productName;

    private Boolean salesYn;

    @Builder
    public ProductSearch(Long productId, String productName, Boolean salesYn) {
        this.productId = productId;
        this.productName = productName;
        this.salesYn = salesYn;
    }

}
