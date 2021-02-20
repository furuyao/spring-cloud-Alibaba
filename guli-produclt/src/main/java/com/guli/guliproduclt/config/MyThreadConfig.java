package com.guli.guliproduclt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/2
 */
@Configuration
public class MyThreadConfig {

        /**
        *@Author: fry
        *@Description: 线程池配置
        *@Param: []
        *@Date: 2020/9/2 14:12
        */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties properties) {

        return new ThreadPoolExecutor(properties.getCoreSize(), properties.getMaxSize(), properties.getKeepAliveTime(), TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.AbortPolicy());
    }

}
