package com.guli.giliorder.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/10
 */

@Configuration
public class MyMQconfig {

    /**
     *@Author: fry
     *@Description: 死信队列
     *@Param: []
     *@Date: 2020/9/10 21:40
     */
    @Bean
    public Queue orderDelayQueue(){


        Map<String,Object> arguments =new HashMap<>();
        // 设置路由键
        arguments.put("x-dead-letter-exchange", "order-event-exchange");
        // 设置死信后的路由键
        arguments.put("x-dead-letter-routing-key", "order.release.order");
        // 设置死信时间 毫秒为单位
        arguments.put("x-message-ttl", 60000);
        Queue queue = new Queue("order.delay.queue", true, false, false,arguments);
        return queue;
    }


    /**
    *@Author: fry
    *@Description: 被消费的队列
    *@Param: []
    *@Date: 2020/9/10 21:41
    */
    @Bean
    public Queue orderDelayOrderQueue(){
        Queue queue = new Queue("order.release.order.queue", true, false, false);
        return queue;

    }
    /**
     *@Author: fry
     *@Description: 交换机
     *@Param: []
     *@Date: 2020/9/10 21:42
     */
    @Bean
    public Exchange orderEventExchange(){

        return new TopicExchange("order-event-exchange",true,false);

    }




    /**
    *@Author: fry
    *@Description: 死信队列和交换机的绑定关系
    *@Param: []
    *@Date: 2020/9/10 21:44
    */
    @Bean
    public Binding orderCreateOrderBinding (){
        //String destination, DestinationType destinationType, String exchange, String routingKey,
        //			@Nullable Map<String, Object> arguments



      return   new Binding("order.delay.queue",Binding.DestinationType.QUEUE,
                "order-event-exchange","order.create.order",null);
    }



    /**
     *@Author: fry 
     *@Description: 消费队列和交换机的绑定关系
     *@Param: []
     *@Date: 2020/9/10 21:44
     */
    @Bean
    public Binding orderReleaseOrderBinding (){

        return   new Binding("order.release.order.queue",Binding.DestinationType.QUEUE,
                "order-event-exchange","order.release.order",null);
    }


    /**
     *@Author: fry
     *@Description: 订单解绑后发消息给库存系统主动解绑
     *@Param: []
     *@Date: 2020/9/10 21:44
     */
    @Bean
    public Binding orderReleaseOtherBinding (){

        return   new Binding("stock.release.stock.queue",Binding.DestinationType.QUEUE,
                "order-event-exchange","order.release.other.#",null);
    }

    /**
     *@Author: fry
     *@Description: 秒杀队列
     *@Param: []
     *@Date: 2020/9/10 21:41
     */
    @Bean
    public Queue orderSeckillOrderQueue() {
        return new Queue("order.seckill.order.queue", true, false, false);
    }

    @Bean
    public Binding orderSeckillOrderQueueBinding() {
        return new Binding("order.seckill.order.queue",
                Binding.DestinationType.QUEUE,
                "order-event-exchange",
                "order.seckill.order",
                new HashMap<>());
    }


}
