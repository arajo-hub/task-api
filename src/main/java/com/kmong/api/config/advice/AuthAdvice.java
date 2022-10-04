package com.kmong.api.config.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthAdvice {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity deniedAccess() {
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

}
