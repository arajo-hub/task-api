package com.kmong.api.common.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationExceptionResponse extends Response {

    private Map<String, String> validation = new HashMap<>();

    @Builder
    public ValidationExceptionResponse(String code, String message, Map<String, String> validation) {
        super(code, message);
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

}