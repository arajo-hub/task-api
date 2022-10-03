package com.kmong.api.order.service.impl;

import com.kmong.api.member.domain.Member;
import com.kmong.api.member.service.MemberService;
import com.kmong.api.order.domain.Order;
import com.kmong.api.order.domain.OrderProduct;
import com.kmong.api.order.repository.OrderRepository;
import com.kmong.api.order.request.OrderCreate;
import com.kmong.api.order.service.OrderService;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.service.ProductService;
import com.querydsl.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final MemberService memberService;

    private final ProductService productService;

    /**
     * 주문 생성
     * @param orderCreate 생성할 주문정보
     * @return 주문 생성 결과
     */
    @Override
    public ResponseEntity createOrder(OrderCreate orderCreate) {
        ResponseEntity response = new ResponseEntity(HttpStatus.OK);
        Optional<Member> memberFindById = memberService.findById(orderCreate.getMemberId());
        if (memberFindById.isPresent()) {
            // 상품정보 가져오기
            List<Product> products = productService.findByIds(orderCreate.getOrderedProductIds());
            List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
            // 상품정보 -> 주문상품으로 변경
            for (Product product : products) {
                // 생성 요청된 주문정보에서 같은 상품 아이디의 주문수량 찾기
                Optional<Integer> orderQuantity = orderCreate.getOrderedProducts().stream().filter(p -> product.getId().equals(p.getId())).map(p -> p.getQuantity()).findFirst();
                if (orderQuantity.isPresent()) {
                    orderProducts.add(OrderProduct.builder()
                                                    .product(product)
                                                    .quantity(orderQuantity.get())
                                                    .price(product.getPrice())
                                                    .build());
                } else {
                    response = new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            }
            Order order = orderCreate.toOrder(memberFindById.get(), orderProducts);
            orderRepository.createOrder(order);
        } else {
            response = new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    /**
     * 주문내역 전체 조회
     * @param id 주문내역을 조회할 회원 아이디
     * @return 주문내역 리스트
     */
    @Override
    public ResponseEntity findAllOrder(String id) {
        List<Order> orders = new ArrayList<Order>();
        if (!StringUtils.isNullOrEmpty(id)) {
            orders = orderRepository.findAllOrder(id);
        }
        log.info("주문수" + orders.size());
        return new ResponseEntity(orders, HttpStatus.OK);
    }
}
