package com.kmong.api.order.request;

import com.kmong.api.member.domain.Member;
import com.kmong.api.order.domain.Order;
import com.kmong.api.order.domain.OrderProduct;
import com.kmong.api.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class OrderCreate {

    private String memberId;
    private List<OrderedProduct> orderedProducts;

    @Builder
    public OrderCreate(String memberId, List<OrderedProduct> orderedProducts) {
        this.memberId = memberId;
        this.orderedProducts = orderedProducts;
    }

    public Order toOrder(Member member, List<OrderProduct> orderProducts) {
        return Order.builder()
                .member(member)
                .orderProducts(orderProducts)
                .orderDatetime(LocalDateTime.now())
                .build();
    }

    public List<Long> getOrderedProductIds() {
        return orderedProducts.stream().map(product -> product.getId()).collect(Collectors.toList());
    }
}
