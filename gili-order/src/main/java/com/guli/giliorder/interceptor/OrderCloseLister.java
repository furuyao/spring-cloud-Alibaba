package com.guli.giliorder.interceptor;

import com.guli.giliorder.entity.OrderEntity;
import com.guli.giliorder.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/11
 */
@RabbitListener(queues = "order.release.order.queue")
@Service
public class OrderCloseLister {

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity orderEntity, Channel channel, Message message) throws IOException {

        try {

            orderService.cloxseOrder(orderEntity);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);

        }catch (Exception e){

            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);


        }

    }


}
