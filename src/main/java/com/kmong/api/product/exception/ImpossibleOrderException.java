package com.kmong.api.product.exception;

import com.kmong.api.common.exception.KmongNotFoundListException;
import lombok.Builder;

import java.util.List;

public class ImpossibleOrderException extends KmongNotFoundListException {

    private static final String MESSAGE = "주문 불가능 상품이 포함되어 주문할 수 없습니다.";

    @Builder
    public ImpossibleOrderException(List list) {
        super(MESSAGE, list);
    }
}