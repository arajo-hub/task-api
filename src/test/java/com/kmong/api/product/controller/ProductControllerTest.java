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
                        .productName("?????? ??????")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build(),
                Product.builder()
                        .productName("????????? ??????")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.FALSE)
                        .build(),
                Product.builder()
                        .productName("????????? ????????? ?????????")
                        .quantity(1)
                        .price(20000)
                        .salesYn(Boolean.TRUE)
                        .build()
        );
        productRepository.saveAll(products);
    }

    @Test
    @DisplayName("?????? ???????????? ??????")
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
    @DisplayName("??????????????? ??????")
    void findByProductName() throws Exception {
        String keyword = "??????";

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
    @DisplayName("?????? ????????? ?????? ??????")
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
    @DisplayName("?????? ??????")
    void createProduct() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                                                    .productName("????????? ????????? ?????????")
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
    @DisplayName("??????????????? ?????? ?????? ??????")
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
    @DisplayName("??? ??????????????? ?????? ?????? ??????")
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
    @DisplayName("30????????? ??????????????? ?????? ?????? ??????")
    void createProductWithOversizeProductName() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("30?????????30?????????30?????????30?????????30?????????30?????????30?????????")
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
    @DisplayName("???????????? ?????? ?????? ??????")
    void createProductWithoutQuantity() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("????????? ????????? ?????????")
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
    @DisplayName("?????? ??????????????? ?????? ?????? ??????")
    void createProductWithMinusQuantity() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("????????? ????????? ?????????")
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
    @DisplayName("?????? 0?????? ?????? ?????? ??????")
    void createProductWithZeroQuantity() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("????????? ????????? ?????????")
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
    @DisplayName("???????????? ?????? ?????? ??????")
    void createProductWithoutPrice() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("????????? ????????? ?????????")
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
    @DisplayName("?????? ??????????????? ?????? ?????? ??????")
    void createProductWithMinusPrice() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("????????? ????????? ?????????")
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
    @DisplayName("?????? 0?????? ?????? ?????? ??????")
    void createProductWithZeroPrice() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("????????? ????????? ?????????")
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
    @DisplayName("?????????????????? ?????? ?????? ??????")
    void createProductWithoutSalesYn() throws Exception {
        ProductCreate productCreate = ProductCreate.builder()
                .productName("????????? ????????? ?????????")
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
