package com.kmong.api.product.controller;

import com.kmong.api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/product/{id}")
    public ResponseEntity findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @GetMapping("/product/name")
    public ResponseEntity findByProductName(String productName) {
        return productService.findByProductName(productName);
    }

    @GetMapping("/product/salesyn")
    public ResponseEntity findBySalesYn(String salesYn) {
        Boolean sales = "Y".equalsIgnoreCase(salesYn) ? Boolean.TRUE
                                                        : "N".equalsIgnoreCase(salesYn) ? Boolean.FALSE : null;
        return productService.findBySalesYn(sales);
    }

}
