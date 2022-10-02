package com.kmong.api.product.service.impl;

import com.kmong.api.product.repository.ProductRepository;
import com.kmong.api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * 아이디로 상품 검색
     * @param id 검색할 상품 아이디
     * @return 검색결과
     */
    @Override
    public ResponseEntity findById(Long id) {
        return new ResponseEntity(productRepository.findById(id), HttpStatus.OK);
    }

    /**
     * 상품명으로 상품 검색(like 검색)
     * @param keyword 검색키워드
     * @return 검색결과
     */
    @Override
    public ResponseEntity findByProductName(String keyword) {
        return new ResponseEntity(productRepository.findByProductName(keyword), HttpStatus.OK);
    }

    /**
     * 판매여부로 상품 검색
     * @param salesYn 판매여부
     * @return 검색결과
     */
    @Override
    public ResponseEntity findBySalesYn(Boolean salesYn) {
        return new ResponseEntity(productRepository.findBySalesYn(salesYn), HttpStatus.OK);
    }

}
