package com.kmong.api.product.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmong.api.member.request.MemberSearch;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class ProductControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private static List<Product> products;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @BeforeEach
    void init() {
        products = List.of(Product.builder()
                        .productName("명함 제작")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build(),
                Product.builder()
                        .productName("청첩장 제작")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.FALSE)
                        .build(),
                Product.builder()
                        .productName("유튜브 백만뷰 노하우")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build()
        );
        productRepository.saveAll(products);
    }

    @Test
    @DisplayName("상품 아이디로 조회")
    void findById() throws Exception {
        Product savedProduct = products.get(0);
        mockMvc.perform(get(String.format("/product/list/%d", savedProduct.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.productName").value(savedProduct.getProductName()));
    }

    @Test
    @DisplayName("상품명으로 조회")
    void findByProductName() throws Exception {
        String keyword = "명함";
        MvcResult result = mockMvc.perform(get("/product/list/name")
                        .param("productName", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        List<Product> searchProducts = objectMapper.readValue(jsonContent, new TypeReference<List<Product>>() {});

        assertEquals(products.stream().filter(p -> p.getProductName().contains(keyword)).count(), searchProducts.size());
    }

    @Test
    @DisplayName("판매 여부로 상품 조회")
    void findBySalesYn() throws Exception {
        MvcResult result = mockMvc.perform(get("/product/list/salesyn")
                        .param("salesYn", "Y")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        List<Product> searchProducts = objectMapper.readValue(jsonContent, new TypeReference<List<Product>>() {});

        assertEquals(products.stream().filter(p -> p.isSalesYn() == Boolean.TRUE).count(), searchProducts.size());
    }
}
