package com.kmong.api.config;

import com.kmong.api.config.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String LOGIN_URL = "/member/login";
    public static final String JOIN_URL = "/member/join";

    public static final String DB_URL = "/h2-console/*";

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .excludePathPatterns(LOGIN_URL, JOIN_URL, DB_URL)
                .excludePathPatterns("/swagger-resources/**", "/webjars/**",
                        "/v2/**", "/swagger-ui.html/**");;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }
}