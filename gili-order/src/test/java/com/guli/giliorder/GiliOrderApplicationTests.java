package com.guli.giliorder;

import com.guli.giliorder.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GiliOrderApplicationTests {
    @Autowired
    private OrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Test
    void contextLoads() {
    }

    /**
     * 创建交换机
     */
    @Test
    void createExchane(){
        //创建一个直接交换机
        /*
            String name 交换机名称
            boolean durable 是否持久化
            boolean autoDelete 是否自动删除
            Map<String, Object> arguments 指定参数
        */
        DirectExchange directExchange = new DirectExchange("hello-java-exchane",true,false,null);
        amqpAdmin.declareExchange(directExchange);
        System.out.println("hello-java-exchane交换机创建完成");
    }
    /**
     * 创建队列
     */
    @Test
    void createQueue(){
        //创建一个普通队列
        /*
            String name 队列名称
            boolean durable 是否持久化
            boolean exclusive 是否排他，队列将仅由声明他的对象使用
            boolean autoDelete 是否自动删除
            Map<String, Object> arguments 指定参数
        */
        Queue queue = new Queue("hello-java-queue",true,false,false,null);
        amqpAdmin.declareQueue(queue);
        System.out.println("hello-java-queue队列创建完成");
    }

}
