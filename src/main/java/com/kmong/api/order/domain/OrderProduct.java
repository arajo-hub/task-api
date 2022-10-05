package com.kmong.api.order.domain;

import com.kmong.api.order.response.OrderProductView;
import com.kmong.api.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String orderProductName;
    @OneToOne
    private Product product;
    @Column
    private int quantity;
    @Column
    private int price;

    @Builder
    public OrderProduct(String orderProductName, Product product, int quantity, int price) {
        this.orderProductName = orderProductName;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderProductView toOrderProductView() {
        return OrderProductView.builder()
                                .productId(product.getId())
                                .productName(orderProductName)
                                .quantity(quantity)
                                .price(price)
                                .build();
    }
}
