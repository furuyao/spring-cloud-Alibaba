package com.guli.gulimember.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/8
 */
@Configuration
public class GuliFeignConfig {


    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {

                /**
                 * 把以前的Cookie放到新请求中去   原理就是运用了同意线程数据共享   ThreadLocal
                 */
                ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                HttpServletRequest request = attributes.getRequest();
                    if (request !=null){
                        String cookie = request.getHeader("Cookie");

                        template.header("Cookie", cookie);
                    }


            }
        };
    }

}
