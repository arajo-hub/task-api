package com.kmong.api.config.advice;

import com.kmong.api.common.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthAdvice {

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity deniedAccess() {
        Response response = new Response("401", "로그인 후 이용 가능합니다.");
        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }

}
