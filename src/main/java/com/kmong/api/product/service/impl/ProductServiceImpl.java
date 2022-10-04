package com.kmong.api.product.service.impl;

import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import com.kmong.api.product.response.ProductView;
import com.kmong.api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<ProductView> findById(Long id) {
        Optional<Product> productFindById = productRepository.findById(id);
        ProductView productView = productFindById.isPresent() ? productFindById.get().toProductView() : null;
        return new ResponseEntity(productView, HttpStatus.OK);
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
    public ResponseEntity<ProductView> findByProductName(String keyword) {
        List<Product> productFindByProductName = productRepository.findByProductName(keyword);
        List<ProductView> productViews = productFindByProductName.stream().map(p -> p.toProductView()).collect(Collectors.toList());
        return new ResponseEntity(productViews, HttpStatus.OK);
    }

    /**
     * 판매여부로 상품 검색
     * @param salesYn 판매여부
     * @return 검색결과
     */
    @Override
    public ResponseEntity<ProductView> findBySalesYn(Boolean salesYn) {
        List<Product> productFindBySalesYn = productRepository.findBySalesYn(salesYn);
        List<ProductView> productViews = productFindBySalesYn.stream().map(p -> p.toProductView()).collect(Collectors.toList());
        return new ResponseEntity(productViews, HttpStatus.OK);
    }

}
