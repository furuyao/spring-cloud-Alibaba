package com.guli.gulike.cart.config;

import com.guli.gulike.cart.interceptor.CarInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/6
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {

    @Autowired
    CarInterceptor carInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(carInterceptor).addPathPatterns("/**");
    }

}

