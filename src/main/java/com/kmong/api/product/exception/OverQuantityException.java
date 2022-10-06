package com.kmong.api.product.exception;

import com.kmong.api.common.exception.KmongApiException;

public class OverQuantityException extends KmongApiException {

    private static final String MESSAGE = "주문수량이 상품수량보다 많습니다.";

    public OverQuantityException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
