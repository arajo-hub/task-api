package com.kmong.api.product.exception;

import com.kmong.api.common.exception.KmongApiListException;
import lombok.Builder;

import java.util.List;

public class ProductsNotFoundException extends KmongApiListException {

    private static final String MESSAGE = "존재하지 않는 상품입니다.";

    @Builder
    public ProductsNotFoundException(List list) {
        super(MESSAGE, list);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}