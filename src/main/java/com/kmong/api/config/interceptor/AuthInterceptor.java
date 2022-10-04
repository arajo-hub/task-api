package com.kmong.api.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

    public static final String LOGIN_URL = "/login";

    public boolean preHandle(HttpServletResponse response, HttpSession session) throws Exception {
        if (session == null || session.getAttribute("id") == null) {
            throw new IllegalAccessException();
        }
        return true;
    }

}
