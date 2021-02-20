package com.guli.giliorder.interceptor;

import com.guli.common.to.mq.SeckillOrderTO;
import com.guli.giliorder.service.OrderService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Message;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @创建人: fry
 * @用于： 秒杀
 * @创建时间 2020/9/14
 */
@Slf4j
@Component
@RabbitListener(queues = "order.seckill.order.queue")
public class OrderSeckillLostener {
    @Resource
    private OrderService orderService;

    @RabbitHandler
    public void listener(SeckillOrderTO seckillOrder, Channel channel, Message message) throws IOException {
        try {
            log.info("准备创建秒杀订单的详细信息...");
            orderService.createSeckillOrder(seckillOrder);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

}
