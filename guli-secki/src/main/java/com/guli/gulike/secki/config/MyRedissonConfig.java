package com.guli.gulike.secki.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/26
 */
@Configuration
public class MyRedissonConfig {


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        // 创建配置
        Config config = new Config();
        // 创建实例
        config.useSingleServer().setAddress("redis://192.168.37.134:6379");
        return Redisson.create(config);

    }

}
