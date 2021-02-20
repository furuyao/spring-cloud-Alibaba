package com.guli.gulike.secki.scheduled;

import com.guli.gulike.secki.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
@Service
@Slf4j
@EnableScheduling
public class SeckillSkuScheduled {

        @Autowired
        SeckillService seckillService;

        @Autowired
        RedissonClient redissonClient;

        private final  String upload_lock = "seckill:upload:lock";

        @Scheduled(cron = "0 * * * * ? ")
        public void updeleadSecKilSkuLatest3Days(){
                // 1重复上架就不用了用分布式锁

                RLock lock = redissonClient.getLock(upload_lock);
                lock.lock(10, TimeUnit.SECONDS);

                try {

                        seckillService.updeleadSecKilSkuLatest3Days();
                }finally {

                        lock.unlock();


                }


        }



}
