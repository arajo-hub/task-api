package com.kmong.api.product.request;

import com.kmong.api.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class ProductCreate {

    @NotBlank(message = "상품명은 필수항목입니다.")
    @Size(min = 1, max = 30, message = "상품명은 1자부터 30자까지 가능합니다.")
    private String productName;

    @NotNull(message = "수량은 필수항목입니다.")
    @Positive(message = "수량은 양수만 입력 가능합니다.")
    private Integer quantity;

    @NotNull(message = "가격은 필수항목입니다.")
    @Min(value = 0, message = "가격은 0부터 입력 가능합니다.")
    private Integer price;

    @NotNull
    private Boolean salesYn;

    @Builder
    public ProductCreate(String productName, Integer quantity, Integer price, Boolean salesYn) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.salesYn = salesYn;
    }

    public Product toProduct() {
        return Product.builder()
                .productName(productName)
                .quantity(quantity)
                .price(price)
                .salesYn(salesYn)
                .build();
    }

}
