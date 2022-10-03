package com.kmong.api.product.service.impl;

import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import com.kmong.api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * 아이디 리스트로 상품 리스트 검색
     * @param productIds 검색할 상품 아이디 리스트
     * @return 검색한 상품 리스트
     */
    @Override
    public List<Product> findByIds(List<Long> productIds) {
        List<Product> products = new ArrayList<Product>();
        for (Long productId : productIds) {
            if (!ObjectUtils.isEmpty(productId)) {
                Optional<Product> productFindById = productRepository.findById(productId);
                if (productFindById.isPresent()) {
                    products.add(productFindById.get());
                }
            } else {
                products = null;
            }
        }
        return products;
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
