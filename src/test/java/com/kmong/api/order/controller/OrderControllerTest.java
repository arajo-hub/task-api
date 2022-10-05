package com.kmong.api.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmong.api.common.response.ListResponse;
import com.kmong.api.common.response.SingleResponse;
import com.kmong.api.common.response.ValidationExceptionResponse;
import com.kmong.api.member.domain.Member;
import com.kmong.api.member.repository.MemberRepository;
import com.kmong.api.order.request.OrderCreate;
import com.kmong.api.order.request.OrderedProduct;
import com.kmong.api.order.response.OrderView;
import com.kmong.api.order.service.OrderService;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class OrderControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private MockHttpSession session;

    private static Member testMember;

    private static List<Product> testProducts;

    private static int tryTime = 3;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        session = new MockHttpSession(null);
        session.setAttribute("id", "test1234");
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

        for (int i = 0; i < tryTime; i++) {
            List<OrderedProduct> products = List.of(OrderedProduct.builder()
                            .productId(testProducts.get(0).getId())
                            .quantity(1)
                            .build(),
                    OrderedProduct.builder()
                            .productId(testProducts.get(1).getId())
                            .quantity(10)
                            .build());
            OrderCreate orderCreate = OrderCreate.builder().memberId(testMember.getId()).orderedProducts(products).build();

            orderService.createOrder(orderCreate);
        }
    }

    @Test
    @DisplayName("주문 완료")
    void createOrder() throws Exception {
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
                                                .orderedProducts(products)
                                                .build();

        MvcResult result = mockMvc.perform(post("/order/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(orderCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        SingleResponse<OrderView> orders = objectMapper.readValue(jsonContent, new TypeReference<SingleResponse<OrderView>>() {});
        assertEquals(products.size(), orders.getObject().getOrderProductViews().size());
    }

    @Test
    @DisplayName("회원아이디없이 주문 시도")
    void createOrderWithoutMemberId() throws Exception {
        List<OrderedProduct> products = List.of(OrderedProduct.builder()
                        .productId(testProducts.get(0).getId())
                        .quantity(1)
                        .build(),
                OrderedProduct.builder()
                        .productId(testProducts.get(1).getId())
                        .quantity(10)
                        .build());

        OrderCreate orderCreate = OrderCreate.builder()
                .orderedProducts(products)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/order/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(orderCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("memberId", key);
        });
    }

    @Test
    @DisplayName("주문내역 조회")
    void findAllOrder() throws Exception {
        MvcResult result = mockMvc.perform(get("/order/list")
                        .session(session)
                        .param("memberId", testMember.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        ListResponse<OrderView> orders = objectMapper.readValue(jsonContent, new TypeReference<ListResponse<OrderView>>() {});
        assertEquals(tryTime, orders.getObjects().size());
    }

}
