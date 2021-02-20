package com.guli.gulike.aoth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession //整合redis作为session存储
@EnableDiscoveryClient
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class})
@EnableFeignClients
public class GuliAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuliAuthServerApplication.class, args);
    }

}
