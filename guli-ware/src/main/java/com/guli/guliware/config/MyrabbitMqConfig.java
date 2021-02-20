package com.guli.guliware.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/11
 */
@Configuration
public class MyrabbitMqConfig {

    /**
     * @Author: fry
     * @Description: mq的JOSN序列化
     * @Param: []
     * @Date: 2020/9/8 10:12
     */
    @Bean
    public MessageConverter getMessageConverter() {

        return new Jackson2JsonMessageConverter();


    }

    /**
     * @Author: fry
     * @Description: 创建交换机
     * @Param: []
     * @Date: 2020/9/11 10:11
     */
    @Bean
    public Exchange stockEventExchange() {

        return new TopicExchange("stock-event-exchange", true, false);

    }

    @Bean
    public Queue stockEventQueue() {

        return new Queue("stock.release.stock.queue", true, false, false);

    }

    @Bean
    public Queue stockDelayQueue() {
        /**
         *     Map<String,Object> arguments =new HashMap<>();
         *         // 信死了交给那个交换机
         *         arguments.put("x-dead-letter-exchange", "order-event-exchange");
         *         // 设置死信后用的路由键
         *         arguments.put("x-dead-letter-routing-key", "order.release.order");
         *         // 设置死信时间 毫秒为单位
         *         arguments.put("x-message-ttl", 60000);
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "stock-event-exchange");
        arguments.put("x-dead-letter-routing-key", "order.release");
        arguments.put("x-message-ttl", 120000);
        return new Queue("stock.delay.queue", true, false, false,arguments);

    }

    @Bean
    public Binding stockReleaseBinding() {

        // 普通队列关系
        return new Binding("stock.release.stock.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.release.#", null);
    }


    @Bean
    public Binding stockLocedBinding() {

        // 死信队列关系
        return new Binding("stock.delay.queue", Binding.DestinationType.QUEUE,
                "stock-event-exchange", "stock.locked", null);
    }


}
