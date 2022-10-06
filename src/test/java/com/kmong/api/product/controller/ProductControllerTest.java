package com.kmong.api.product.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kmong.api.common.response.ListResponse;
import com.kmong.api.common.response.SingleResponse;
import com.kmong.api.common.response.ValidationExceptionResponse;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import com.kmong.api.product.request.ProductCreate;
import com.kmong.api.product.request.ProductSearch;
import com.kmong.api.product.response.ProductView;
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
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private MockHttpSession session;

    private static List<Product> products;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        session = new MockHttpSession(null);
        session.setAttribute("id", "test1234");
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
        ProductSearch productSearch = ProductSearch.builder()
                                                    .productId(products.get(0).getId())
                                                    .build();

        mockMvc.perform(get("/product/list")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productSearch))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("상품명으로 조회")
    void findByProductName() throws Exception {
        String keyword = "명함";

        MvcResult result = mockMvc.perform(get("/product/list")
                        .session(session)
                        .param("productName", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isOk())
                        .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        ListResponse<ProductView> searchProducts = objectMapper.readValue(jsonContent, new TypeReference<ListResponse<ProductView>>() {});

        assertEquals(products.stream().filter(p -> p.getProductName().contains(keyword)).count(), searchProducts.getDatas().size());
    }

    @Test
    @DisplayName("판매 여부로 상품 조회")
    void findBySalesYn() throws Exception {

        MvcResult result = mockMvc.perform(get("/product/list")
                        .session(session)
                        .param("salesYn", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();

        String jsonContent = result.getResponse().getContentAsString();
        ListResponse<ProductView> searchProducts = objectMapper.readValue(jsonContent, new TypeReference<ListResponse<ProductView>>() {});

        assertEquals(products.stream().filter(p -> p.isSalesYn() == Boolean.TRUE).count(), searchProducts.getDatas().size());
    }

    @Test
    @DisplayName("상품 등록")
    void createProduct() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                                                    .productName("유튜브 백만뷰 노하우")
                                                    .quantity(500)
                                                    .price(40000)
                                                    .salesYn(true)
                                                    .build();

        MvcResult result = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andDo(MockMvcResultHandlers.print())
                        .andReturn();


        SingleResponse<ProductView> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<SingleResponse<ProductView>>() {});
        assertTrue(!ObjectUtils.isEmpty(response));
    }

    @Test
    @DisplayName("상품명없이 상품 등록 시도")
    void createProductWithoutProductName() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                                                    .quantity(500)
                                                    .price(40000)
                                                    .salesYn(true)
                                                    .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andDo(MockMvcResultHandlers.print())
                        .andExpect(status().isBadRequest())
                        .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("productName", key);
        });
    }

    @Test
    @DisplayName("빈 상품명으로 상품 등록 시도")
    void createProductWithEmptyProductName() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("")
                .quantity(500)
                .price(40000)
                .salesYn(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("productName", key);
        });
    }

    @Test
    @DisplayName("30자이상 상품명으로 상품 등록 시도")
    void createProductWithOversizeProductName() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("30자이상30자이상30자이상30자이상30자이상30자이상30자이상")
                .quantity(500)
                .price(40000)
                .salesYn(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("productName", key);
        });
    }

    @Test
    @DisplayName("수량없이 상품 등록 시도")
    void createProductWithoutQuantity() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("유튜브 백만뷰 노하우")
                .price(40000)
                .salesYn(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("quantity", key);
        });
    }

    @Test
    @DisplayName("수량 마이너스로 상품 등록 시도")
    void createProductWithMinusQuantity() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("유튜브 백만뷰 노하우")
                .quantity(-100)
                .price(40000)
                .salesYn(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("quantity", key);
        });
    }

    @Test
    @DisplayName("수량 0으로 상품 등록 시도")
    void createProductWithZeroQuantity() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("유튜브 백만뷰 노하우")
                .quantity(0)
                .price(40000)
                .salesYn(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("quantity", key);
        });
    }

    @Test
    @DisplayName("가격없이 상품 등록 시도")
    void createProductWithoutPrice() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("유튜브 백만뷰 노하우")
                .quantity(500)
                .salesYn(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("price", key);
        });
    }

    @Test
    @DisplayName("가격 마이너스로 상품 등록 시도")
    void createProductWithMinusPrice() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("유튜브 백만뷰 노하우")
                .quantity(500)
                .price(-10000)
                .salesYn(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("price", key);
        });
    }

    @Test
    @DisplayName("가격 0으로 상품 등록 시도")
    void createProductWithZeroPrice() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("유튜브 백만뷰 노하우")
                .quantity(500)
                .price(0)
                .salesYn(true)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String jsonContent = mvcResult.getResponse().getContentAsString();
        SingleResponse<ProductView> savedProduct = objectMapper.readValue(jsonContent, new TypeReference<SingleResponse<ProductView>>() {});
        assertTrue(!ObjectUtils.isEmpty(savedProduct));
    }

    @Test
    @DisplayName("판매여부없이 상품 등록 시도")
    void createProductWithoutSalesYn() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("유튜브 백만뷰 노하우")
                .quantity(500)
                .price(40000)
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/product/create")
                        .session(session)
                        .content(objectMapper.writeValueAsString(productCreate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        ValidationExceptionResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ValidationExceptionResponse.class);
        Map<String, String> validation = response.getValidation();
        validation.forEach((key, value) -> {
            assertEquals("salesYn", key);
        });
    }

}
