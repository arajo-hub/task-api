package com.kmong.api.product.service;

import com.kmong.api.product.domain.Product;
import com.kmong.api.product.request.ProductCreate;
import com.kmong.api.product.request.ProductSearch;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    /**
     * 아이디로 상품 검색
     * @param id 검색할 상품 아이디
     * @return 검색결과
     */
    ResponseEntity findById(Long id);

    /**
     *
     * @param productSearch
     * @return
     */
    ResponseEntity findAll(ProductSearch productSearch);

    /**
     * 아이디 리스트로 상품 리스트 검색
     * @param productIds 검색할 상품 아이디 리스트
     * @return 검색한 상품 리스트
     */
    List<Product> findByIds(List<Long> productIds);

    /**
     * 상품 등록
     * @param productCreate 등록할 상품정보
     * @return 등록결과
     */
    ResponseEntity createProduct(ProductCreate productCreate);
}
