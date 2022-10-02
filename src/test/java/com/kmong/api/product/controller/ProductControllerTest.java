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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @BeforeEach
    @AfterEach
    void clearAll() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 아이디로 조회")
    void findById() throws Exception {
        Product savedProduct = Product.builder()
                .productName("명함 제작")
                .quantity(1)
                .price(20000)
                .salesYn(true)
                .build();
        productRepository.save(savedProduct);

        mockMvc.perform(get(String.format("/product/%d", savedProduct.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.productName").value(savedProduct.getProductName()));
    }

    @Test
    @DisplayName("상품명으로 조회")
    void findByProductName() throws Exception {
        Product target = Product.builder()
                                .productName("명함 제작")
                                .quantity(1)
                                .price(20000)
                                .salesYn(Boolean.TRUE)
                                .build();
        List<Product> products = List.of(target,
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
        productRepository.saveAll(products);

        MvcResult result = mockMvc.perform(get("/product/name")
                        .param("productName", "명함")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        List<Product> searchProducts = objectMapper.readValue(jsonContent, new TypeReference<List<Product>>() {});

        assertEquals(1, searchProducts.size());
    }

    @Test
    @DisplayName("판매 여부로 상품 조회")
    void findBySalesYn() throws Exception {
        List<Product> products = List.of(Product.builder()
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

        MvcResult result = mockMvc.perform(get("/product/salesyn")
                        .param("salesYn", "Y")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        List<Product> searchProducts = objectMapper.readValue(jsonContent, new TypeReference<List<Product>>() {});

        assertEquals(2, searchProducts.size());
    }
}
