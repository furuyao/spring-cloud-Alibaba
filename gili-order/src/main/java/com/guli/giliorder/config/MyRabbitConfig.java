package com.guli.giliorder.config;

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

    RabbitTemplate rabbitTemplate;



    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setMessageConverter(getMessageConverter());
        initRabbitTemplate();
       return rabbitTemplate;

    }

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
//    @PostConstruct   //MyRabbitConfig对象创建完成以后初始化rabbitTemplate，这样就会有消息确认回调
    public void initRabbitTemplate(){

        // 这个是到达交换机的会调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData   当前信息的唯一关联数据（唯一ID）
             * @param ack   消息是否成功收到
             * @param cause  失败原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            }
        });



        //
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {

            /**
            *@Author: fry
            *@Description: 这个是到达队列的回调 并且这个方法只回调没有投递到指定队列才触发
            *@Param: message, 投递失败原因
             *@Param: replyCode, 回复状态码
             *@Param: replyText, 回复文本内容
             *@Param: exchange, 当时消息投递给那个交换机了
             *@Param: routingKey 当时消息用的那个路由键
            *@Date: 2020/9/8 10:38
            */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {



            }
        });

    }

}
