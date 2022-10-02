package com.kmong.api.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clearAll() {
        productRepository.deleteAll();
    }

    @Test
    @DisplayName("상품 아이디로 조회")
    void findById() throws JsonProcessingException {
        Product savedProduct = Product.builder()
                                    .productName("명함 제작")
                                    .quantity(1)
                                    .price(20000)
                                    .salesYn(true)
                                    .build();
        productRepository.save(savedProduct);

        ResponseEntity response = productService.findById(savedProduct.getId());
        Product searchedProduct = objectMapper.readValue(response.getBody().toString(), Product.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedProduct, searchedProduct);
    }

    @Test
    @DisplayName("존재하지 않는 상품 아이디로 조회")
    void findByNotExistsId() {
        ResponseEntity response = productService.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Optional.empty(), response.getBody());
    }

    @Test
    @DisplayName("상품명으로 조회")
    void findByProductName() {
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

        ResponseEntity response = productService.findByProductName("제작");
        List<Product> searchResult = (List<Product>) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, searchResult.size());
    }

    @Test
    @DisplayName("판매여부로 조회")
    void findBySalesYn() {
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

        ResponseEntity response = productService.findBySalesYn(Boolean.FALSE);
        List<Product> searchResult = (List<Product>) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, searchResult.size());
    }

}
