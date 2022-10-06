package com.kmong.api.order.service;

import com.kmong.api.common.response.ListResponse;
import com.kmong.api.member.domain.Member;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.order.repository.OrderRepository;
import com.kmong.api.order.request.OrderCreate;
import com.kmong.api.order.request.OrderSearch;
import com.kmong.api.order.request.OrderedProduct;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private static Member testMember;

    private static List<Product> testProducts;

    @BeforeEach
    void init() {
        testMember = Member.builder()
                .id("test1234")
                .email("test1234@naver.com")
                .pwd("Test@1234")
                .build();
        memberRepository.save(testMember);

        testProducts = List.of(Product.builder()
                        .productName("명함 제작")
                        .quantity(1000)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build(),
                Product.builder()
                        .productName("청첩장 제작")
                        .quantity(1000)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build(),
                Product.builder()
                        .productName("유튜브 백만뷰 노하우")
                        .quantity(1000)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build()
        );
        productRepository.saveAll(testProducts);
    }

    @Test
    @DisplayName("주문 생성")
    void createOrder() {
        List<OrderedProduct> products = List.of(OrderedProduct.builder()
                        .productId(testProducts.get(0).getId())
                        .quantity(1)
                        .build(),
                OrderedProduct.builder()
                        .productId(testProducts.get(1).getId())
                        .quantity(10)
                        .build());
        OrderCreate orderCreate = OrderCreate.builder().memberId(testMember.getId()).orderedProducts(products).build();
        ResponseEntity response = orderService.createOrder(orderCreate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("주문내역 전체 조회")
    void findAllOrder() {
        int tryTime = 3;

        for (int i = 0; i < tryTime; i++) {
            List<OrderedProduct> products = List.of(OrderedProduct.builder()
                                                                    .productId(testProducts.get(0).getId())
                                                                    .quantity(1)
                                                                    .build(),
                                                    OrderedProduct.builder()
                                                                    .productId(testProducts.get(1).getId())
                                                                    .quantity(10)
                                                                    .build());
            OrderCreate orderCreate = OrderCreate.builder()
                    .memberId(testMember.getId())
                    .orderedProducts(products).build();
            ResponseEntity response = orderService.createOrder(orderCreate);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        OrderSearch orderSearch = OrderSearch.builder()
                                                .memberId(testMember.getId())
                                                .build();

        ResponseEntity result = orderService.findAllOrder(orderSearch);
        ListResponse savedOrders = (ListResponse) result.getBody();
        assertEquals(tryTime, savedOrders.getDatas().size());
    }

}