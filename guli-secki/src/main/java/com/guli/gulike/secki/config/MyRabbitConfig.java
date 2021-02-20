package com.guli.gulike.secki.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/7
 */
@Configuration
public class MyRabbitConfig {


    /**
    *@Author: fry
    *@Description: mq的JOSN序列化
    *@Param: []
    *@Date: 2020/9/8 10:12
    */
    @Bean
    public MessageConverter getMessageConverter() {

        return new Jackson2JsonMessageConverter();


    }



}
