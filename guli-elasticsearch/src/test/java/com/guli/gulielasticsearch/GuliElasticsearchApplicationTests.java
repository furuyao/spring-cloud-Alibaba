package com.guli.gulielasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


//@SpringBootTest
class GuliElasticsearchApplicationTests {


    //    @Test
    void contextLoads() throws InterruptedException {

        BlockingQueue<String> stringBlockingQueue = new LinkedBlockingQueue<>();


        // 放一个取一个
        BlockingQueue<String> blockingQueue = new SynchronousQueue<>();
        // 队列是固定的
        BlockingQueue<String> queue = new ArrayBlockingQueue(3);
        // 放不进去等待多少秒返回
        queue.offer("a0", 3, TimeUnit.MICROSECONDS);
        // 取不出来等待多少秒返回
        queue.poll(1, TimeUnit.MINUTES);
        // 阻塞等待放
        queue.put("a");
        // 阻塞等待取
        queue.take();
        // 放不进去抛异常
        queue.add("a2");
        // 取不出来抛异常
        queue.remove();


    }


    /**
     * 传统线程交护
     */

    public static void main(String[] args) {

        ShareData shareData = new ShareData();
        // 负责生产的线程
        new Thread(() -> {

            for (int i = 0; i <= 5; i++) {

                try {
                    shareData.increment();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, "AA").start();

        // 消费线程
        new Thread(() -> {
            for (int i = 0; i <= 5; i++) {

                try {
                    shareData.increment1();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }, "BB").start();


    }

}

// 资源类
class ShareData {

    // 交换标志
    private int number = 0;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    /**
     * 生产者
     */

    public void increment() throws Exception {

        lock.lock();
        try {

            while (number != 0) {

                // 阻塞等待
                condition.await();
            }
            // 改状态
            number++;
            System.out.println(Thread.currentThread().getName());
            // 通知唤醒
            condition.signalAll();

        } catch (Exception e) {

            System.out.println(e.toString());

        } finally {
            // 释放锁
            lock.unlock();


        }


    }

    /**
     * 消费者
     */
    public void increment1() throws Exception {

        lock.lock();
        try {

            while (number == 0) {

                // 阻塞等待
                condition.await();
            }
            // 改状态
            number--;
            System.out.println(Thread.currentThread().getName());
            // 通知唤醒
            condition.signalAll();

        } catch (Exception e) {

            System.out.println(e.toString());

        } finally {
            // 释放锁
            lock.unlock();


        }


    }


}