package com.kmong.api.product.service;

import org.springframework.http.ResponseEntity;

public interface ProductService {

    /**
     * 아이디로 상품 검색
     * @param id 검색할 상품 아이디
     * @return 검색결과
     */
    ResponseEntity findById(Long id);

    /**
     * 상품명으로 상품 검색(like 검색)
     * @param productName 검색어
     * @return  검색결과
     */
    ResponseEntity findByProductName(String productName);

    /**
     * 판매여부로 상품 검색
     * @param salesYn 판매여부
     * @return 검색결과
     */
    ResponseEntity findBySalesYn(Boolean salesYn);
}
