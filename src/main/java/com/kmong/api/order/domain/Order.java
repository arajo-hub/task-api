package com.kmong.api.order.domain;

import com.kmong.api.member.domain.Member;
import com.kmong.api.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    public Order(Member member, List<OrderProduct> orderProducts, LocalDateTime orderDatetime, int totalPrice) {
        this.member = member;
        this.orderProducts = orderProducts;
        this.orderDatetime = orderDatetime;
        this.totalPrice = totalPrice;
    }

}
