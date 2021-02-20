package com.guli.giliorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRabbit
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableFeignClients
@EnableRedisHttpSession //整合redis作为session存储
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.guli.giliorder.dao")
public class GiliOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GiliOrderApplication.class, args);
    }

}
