package com.kmong.api.order.exception;

import com.kmong.api.common.exception.KmongApiException;

public class InconsistentMemberException extends KmongApiException {

    private static final String MESSAGE = "조회권한이 없습니다. 본인의 주문내역만 조회 가능합니다.";

    public InconsistentMemberException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
