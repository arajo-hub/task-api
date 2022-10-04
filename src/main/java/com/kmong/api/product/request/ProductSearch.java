package com.kmong.api.product.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearch {

    private Long id;
    private String productName;
    private String salesYn;

    @Builder
    public ProductSearch(Long id, String productName, String salesYn) {
        this.id = id;
        this.productName = productName;
        this.salesYn = salesYn;
    }

}
