package com.kmong.api.product.service.impl;

import com.kmong.api.common.response.ListResponse;
import com.kmong.api.common.response.SingleResponse;
import com.kmong.api.product.domain.Product;
import com.kmong.api.product.repository.ProductRepository;
import com.kmong.api.product.request.ProductCreate;
import com.kmong.api.product.request.ProductSearch;
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
     * 상품 전체 조회
     * @return 상품 전체 리스트
     */
    @Override
    public ResponseEntity findAll(ProductSearch productSearch) {
        List<Product> products = productRepository.findAll(productSearch);
        List<ProductView> productViews = products.stream().map(p -> p.toProductView()).collect(Collectors.toList());
        ListResponse body = ListResponse.builder()
                                        .code("200")
                                        .message("상품이 조회되었습니다.")
                                        .build();
        body.setDatas(productViews);
        return new ResponseEntity(body, HttpStatus.OK);
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
     * 상품 등록
     * @param productCreate 등록할 상품정보
     * @return 등록결과
     */
    @Override
    public ResponseEntity createProduct(ProductCreate productCreate) {
        Product product = productRepository.createProduct(productCreate.toProduct());
        SingleResponse singleResponse = SingleResponse.builder()
                                                    .code("201")
                                                    .message("상품을 등록했습니다.")
                                                    .data(product.toProductView()).build();
        return new ResponseEntity(singleResponse, HttpStatus.CREATED);
    }

}
