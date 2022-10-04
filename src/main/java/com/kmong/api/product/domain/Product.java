package com.kmong.api.product.domain;

import com.kmong.api.product.response.ProductView;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String productName;
    @Column
    private int quantity;
    @Column
    private int price;
    @Column
    private boolean salesYn;

    @Builder
    public Product(String productName, int quantity, int price, boolean salesYn) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.salesYn = salesYn;
    }

    public ProductView toProductView() {
        return ProductView.builder()
                .id(id)
                .productName(productName)
                .quantity(quantity)
                .price(price)
                .salesYn(salesYn)
                .build();
    }
}
