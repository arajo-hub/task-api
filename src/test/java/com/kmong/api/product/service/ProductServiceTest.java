package com.kmong.api.product.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmong.api.common.response.ListResponse;
import com.kmong.api.common.response.SingleResponse;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import com.kmong.api.product.request.ProductCreate;
import com.kmong.api.product.request.ProductSearch;
import com.kmong.api.product.response.ProductView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

        ProductView searchedProduct = (ProductView) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(!ObjectUtils.isEmpty(searchedProduct));
    }

    @Test
    @DisplayName("존재하지 않는 상품 아이디로 조회")
    void findByNotExistsId() {
        ResponseEntity response = productService.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody());
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

        ProductSearch productSearch = ProductSearch.builder()
                                                    .productName("제작")
                                                    .build();

        ResponseEntity response = productService.findAll(productSearch);
        ListResponse<ProductView> searchResult = (ListResponse<ProductView>) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, searchResult.getObjects().size());
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

        ProductSearch productSearch = ProductSearch.builder()
                                                    .salesYn(false)
                                                    .build();

        ResponseEntity response = productService.findAll(productSearch);
        ListResponse searchResult = (ListResponse) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, searchResult.getObjects().size());
    }

    @Test
    @DisplayName("상품 등록")
    void createProduct() {
        ProductCreate productCreate = ProductCreate.builder()
                                                    .productName("유튜브 백만뷰 노하우")
                                                    .quantity(500)
                                                    .price(40000)
                                                    .salesYn(true)
                                                    .build();
        ResponseEntity response = productService.createProduct(productCreate);
        System.out.println(response.getBody());
        SingleResponse searchedProduct = (SingleResponse) response.getBody();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(!ObjectUtils.isEmpty(searchedProduct.getObject()));
    }

}
