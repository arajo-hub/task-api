package com.kmong.api.order.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@IdClass(OrderProductRelId.class)
public class OrderProductRel {

    @Id
    @OneToOne
    private Order order;
    @Id
    @OneToOne
    private OrderProduct orderProduct;

}
