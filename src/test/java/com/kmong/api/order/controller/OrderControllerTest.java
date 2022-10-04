package com.kmong.api.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmong.api.member.domain.Member;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.order.domain.Order;
import com.kmong.api.order.domain.OrderProduct;
import com.kmong.api.order.repository.OrderRepository;
import com.kmong.api.order.request.OrderCreate;
import com.kmong.api.order.request.OrderedProduct;
import com.kmong.api.order.response.OrderView;
import com.kmong.api.order.service.OrderService;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class OrderControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private static Member testMember;

    private static List<Product> testProducts;

    private static int tryTime = 3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @BeforeEach
    void init() {
        testMember = Member.builder()
                .id("test1234")
                .email("test1234@naver.com")
                .pwd("test1234")
                .sessionId("sessionId")
                .build();
        memberRepository.save(testMember);

        testProducts = List.of(Product.builder()
                        .productName("명함 제작")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build(),
                Product.builder()
                        .productName("청첩장 제작")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build(),
                Product.builder()
                        .productName("유튜브 백만뷰 노하우")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build()
        );
        productRepository.saveAll(testProducts);

        for (int i = 0; i < tryTime; i++) {
            List<OrderedProduct> products = List.of(OrderedProduct.builder()
                            .id(testProducts.get(0).getId())
                            .quantity(1)
                            .build(),
                    OrderedProduct.builder()
                            .id(testProducts.get(1).getId())
                            .quantity(10)
                            .build());
            OrderCreate orderCreate = OrderCreate.builder().memberId(testMember.getId()).orderedProducts(products).build();

            orderService.createOrder(orderCreate);
        }
    }

    @Test
    @DisplayName("주문내역 조회")
    void findAllOrder() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", testMember.getId()))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        List<OrderView> orders = objectMapper.readValue(jsonContent, new TypeReference<List<OrderView>>() {});

        assertEquals(tryTime, orders.size());
    }
}
