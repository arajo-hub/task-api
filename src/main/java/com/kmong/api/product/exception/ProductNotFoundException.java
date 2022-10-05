package com.kmong.api.product.exception;

import com.kmong.api.common.exception.KmongNotFoundException;

public class ProductNotFoundException extends KmongNotFoundException {

    private static final String MESSAGE = "해당하는 상품이 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }

}
