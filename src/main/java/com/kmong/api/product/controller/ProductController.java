package com.kmong.api.product.controller;

import com.kmong.api.product.request.ProductCreate;
import com.kmong.api.product.request.ProductSearch;
import com.kmong.api.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product/create")
    public ResponseEntity createProduct(@RequestBody @Valid ProductCreate productCreate) {
        return productService.createProduct(productCreate);
    }

    @GetMapping("/product/list")
    public ResponseEntity findAll(@RequestBody @Valid ProductSearch productSearch) {
        return productService.findAll(productSearch);
    }

}
