package com.kmong.api.product.exception;

import com.kmong.api.common.exception.KmongNotFoundListException;
import lombok.Builder;

import java.util.List;

public class ProductsNotFoundException extends KmongNotFoundListException {

    private static final String MESSAGE = "존재하지 않는 상품입니다.";

    @Builder
    public ProductsNotFoundException(List list) {
        super(MESSAGE, list);
    }
}