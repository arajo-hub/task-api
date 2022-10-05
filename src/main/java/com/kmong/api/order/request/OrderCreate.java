package com.kmong.api.order.request;

import com.kmong.api.member.domain.Member;
import com.kmong.api.order.domain.Order;
import com.kmong.api.order.domain.OrderProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class OrderCreate {

    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]*$", message = "아이디는 첫시작을 영어로, 구성은 영숫자로만 가능합니다.")
    @NotNull(message = "아이디는 필수항목입니다.")
    @Size(min = 1, max = 15, message = "아이디는 1자부터 15자까지 가능합니다.")
    private String memberId;

    @NotNull(message = "주문하고자 하는 상품이 1개 이상 존재해야 합니다.")
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
        return orderedProducts.stream().map(product -> product.getProductId()).collect(Collectors.toList());
    }
}
