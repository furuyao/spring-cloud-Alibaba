package com.guli.guliware.interceptor;

import com.guli.common.to.mq.OrderInterceptorTo;
import com.guli.common.to.mq.StockLockedTo;
import com.guli.guliware.service.WareSkuService;
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

@Service
@RabbitListener(queues = "stock.release.stock.queue")
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;

    @RabbitHandler
    public void handleOrderRelease(OrderInterceptorTo stockLockedTo, Message message, Channel channel) throws IOException {
        System.out.println("订单关闭准备解锁库存");
        try {

            wareSkuService.unlocOrderRelease(stockLockedTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){

            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);

        }


    }
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo stockLockedTo, Message message, Channel channel) throws IOException {
        System.out.println("收到解锁信息");
        try {

            wareSkuService.unlocStock(stockLockedTo);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){

            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);

        }


    }

}
