package com.kmong.api.product.exception;

import com.kmong.api.common.exception.KmongApiListException;

public class ProductNotFoundException extends KmongApiListException {

    private static final String MESSAGE = "해당하는 상품이 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
