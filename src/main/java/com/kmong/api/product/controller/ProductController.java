package com.kmong.api.product.controller;

import com.kmong.api.product.request.ProductSearch;
import com.kmong.api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity findAll() {
        return productService.findAll();
    }

    @GetMapping("/product/list/id")
    public ResponseEntity findById(ProductSearch productSearch) {
        return productService.findById(productSearch.getId());
    }

    @GetMapping("/product/list/name")
    public ResponseEntity findByProductName(ProductSearch productSearch) {
        return productService.findByProductName(productSearch.getProductName());
    }

    @GetMapping("/product/list/salesyn")
    public ResponseEntity findBySalesYn(ProductSearch productSearch) {
        Boolean sales = "Y".equalsIgnoreCase(productSearch.getSalesYn()) ? Boolean.TRUE
                                                        : "N".equalsIgnoreCase(productSearch.getSalesYn()) ? Boolean.FALSE : null;
        return productService.findBySalesYn(sales);
    }

}
