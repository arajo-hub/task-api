package com.kmong.api.config.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.kmong.api.common.constant.Constant.SESSION_ATTRIBUTE_MEMBER_ID;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session == null || session.getAttribute(SESSION_ATTRIBUTE_MEMBER_ID) == null) {
            throw new IllegalAccessException();
        }
        return true;
    }

}
