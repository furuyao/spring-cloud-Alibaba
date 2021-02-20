package com.guli.gulike.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @创建人: fry
 * @用于：动态配置线程池
 * @创建时间 2020/9/2
 */


@Data
@Component
@ConfigurationProperties(prefix = "guli.thread")
public class ThreadPoolConfigProperties {

    // 核心线程大小
    private Integer coreSize;
    // 最大线程数
    private Integer maxSize;
    // 等待时间
    private Integer keepAliveTime;


}



