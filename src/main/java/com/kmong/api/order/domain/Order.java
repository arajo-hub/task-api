package com.kmong.api.order.domain;

import com.kmong.api.member.domain.Member;
import com.kmong.api.order.response.OrderProductView;
import com.kmong.api.order.response.OrderView;
import com.kmong.api.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Member member;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;
    @Column
    private LocalDateTime orderDatetime;
    @Column
    private int totalPrice;

    @Builder
    public Order(Member member, List<OrderProduct> orderProducts, LocalDateTime orderDatetime) {
        this.member = member;
        this.orderProducts = orderProducts;
        this.orderDatetime = orderDatetime;
        this.totalPrice = calculateTotalPrice();
    }

    public OrderView toOrderView() {
        List<OrderProductView> orderProductViews = orderProducts.stream().map(orderProduct -> orderProduct.toOrderProductView()).collect(Collectors.toList());
        return OrderView.builder()
                        .id(id)
                        .orderProductViews(orderProductViews)
                        .orderDatetime(orderDatetime)
                        .totalPrice(calculateTotalPrice())
                        .build();
    }

    private int calculateTotalPrice() {
        return orderProducts.stream().mapToInt(op -> op.getPrice() * op.getQuantity()).sum();
    }
}
