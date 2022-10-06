package com.kmong.api.config.advice;

import com.kmong.api.common.exception.KmongApiException;
import com.kmong.api.common.exception.KmongApiListException;
import com.kmong.api.common.response.ListResponse;
import com.kmong.api.common.response.Response;
import com.kmong.api.common.response.ValidationExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity hasInvalidParam(MethodArgumentNotValidException e) {
        ValidationExceptionResponse response = ValidationExceptionResponse.builder()
                                                                            .code("400")
                                                                            .message("잘못된 요청입니다.")
                                                                            .validation(new HashMap<>())
                                                                            .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KmongApiListException.class)
    public ResponseEntity kmongNotFoundException(KmongApiListException e) {
        int statusCode = e.getStatusCode();
        ListResponse body = new ListResponse(String.valueOf(statusCode), e.getMessage());
        body.setObjects(e.getList());
        return ResponseEntity.status(statusCode).body(body);
    }

    @ExceptionHandler(KmongApiException.class)
    public ResponseEntity kmongApiException(KmongApiException e) {
        int statusCode = e.getStatusCode();
        Response body = new Response(String.valueOf(statusCode), e.getMessage());
        return ResponseEntity.status(statusCode).body(body);
    }

}
