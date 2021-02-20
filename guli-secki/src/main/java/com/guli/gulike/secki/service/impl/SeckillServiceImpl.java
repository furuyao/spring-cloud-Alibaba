package com.guli.gulike.secki.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guli.common.to.SeckillSessionTo;
import com.guli.common.to.SeckillSkuRelationTo;
import com.guli.common.to.SkuInfoVo;
import com.guli.common.to.mq.SeckillOrderTO;
import com.guli.common.utils.R;
import com.guli.common.vo.MemberRerspVo;
import com.guli.gulike.secki.feign.CouponFeignService;
import com.guli.gulike.secki.feign.ProductFeinService;
import com.guli.gulike.secki.interceptor.LoginUserInterceptor;
import com.guli.gulike.secki.service.SeckillService;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeinService productFeinService;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RabbitTemplate rabbitTemplate;


    private final String SESSIONS_CACHE_PREFIX = "seckill:sessions:";
    private final String SKUKILL_CACHE_PREFIX = "seckill:skus";
    //信号量
    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:"; //+商品随机码


    @Override
    public void updeleadSecKilSkuLatest3Days() {
        // 1.去数据看扫描查看要掺加要秒杀的活动
        R kilSkuLatest3Days = couponFeignService.getKilSkuLatest3Days();

        if (kilSkuLatest3Days.getCode() == 0) {

            List<SeckillSessionTo> data = kilSkuLatest3Days.getData("data", new TypeReference<List<SeckillSessionTo>>() {
            });

            // 缓存活动信息
            saveSessionInfos(data);
            // 缓存商品信息
            saveSkuInfos(data);
        }


    }

    @Override
    public List<SeckillSkuRelationTo> getCurrentSeckillSkus() {

        long time = new Date().getTime();

        Set<String> keys = redisTemplate.keys(SESSIONS_CACHE_PREFIX + "*");
        if (keys != null && keys.size() > 0) {

            for (String item : keys) {
                String replace = item.replace(SESSIONS_CACHE_PREFIX, "");

                String[] s = replace.split("_");

                long start = Long.parseLong(s[0]);

                long end = Long.parseLong(s[1]);

                if (time >= start && time <= end) {

                    // 获取当前场次

                    List<String> range = redisTemplate.opsForList().range(item, -100, 100);

                    BoundHashOperations<String, String, String> boundHashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);

                    List<String> objects = boundHashOps.multiGet(range);

                    if (objects != null) {


                        List<SeckillSkuRelationTo> collect = objects.stream().map(opsen -> {
                            SeckillSkuRelationTo relationTo = JSON.parseObject((String) opsen, SeckillSkuRelationTo.class);
                            return relationTo;
                        }).collect(Collectors.toList());

                        return collect;
                    }
                    break;
                }

            }

        }
        return null;
    }

    @Override
    public SeckillSkuRelationTo getSkuSeckillInfo(Long skuId) {

        BoundHashOperations<String, String, String> boundHashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);

        Set<String> keys = boundHashOps.keys();

        if (keys != null && keys.size() > 0) {

            String regx = "\\d_" + skuId;

            for (String itm : keys) {
                if (Pattern.matches(regx, itm)) {
                    String key = boundHashOps.get(itm);
                    SeckillSkuRelationTo to = JSON.parseObject(key, SeckillSkuRelationTo.class);
                    long time = new Date().getTime();
                    if (time > to.getStartTiem() && to.getEndTiem() >= time) {
                    } else {
                        to.setRandomCode(null);
                    }

                    return to;
                }

            }


        }


        return null;
    }

    @Override
    public String secKill(String killId, String key, Integer num) {

        MemberRerspVo rerspVo = LoginUserInterceptor.local.get();
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);

        String key1 = hashOps.get(killId);
        if (key1 == null) {

            return null;
        } else {

            SeckillSkuRelationTo sessionTo = JSON.parseObject(key1, SeckillSkuRelationTo.class);
            // 校验合法性
            Long startTiem = sessionTo.getStartTiem();

            Long endTiem = sessionTo.getEndTiem();

            long time = new Date().getTime();
            long intervals = endTiem - time;
            // 校验时间合法性
            if (time >= startTiem && time < endTiem) {
                //检验随机码
                String code = sessionTo.getRandomCode();
                String skuId = sessionTo.getPromotionSessionId() + "_" + sessionTo.getSkuId();

                if (code.equals(key) && skuId.equals(killId)) {
                    // 验证数量
                    if (sessionTo.getSeckillLimit().intValue() >= num) {
                        // 验证该用户是否已买过
                        String item = rerspVo.getId() + "_" + skuId;
                        // 自动过期
                        Boolean absent = redisTemplate.opsForValue().setIfAbsent(item, num.toString(), intervals, TimeUnit.MILLISECONDS);
                        if (absent) {
                            // 占位成功
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + code);

                            // 减信号量并设置等待时间
                            boolean b = semaphore.tryAcquire(num);
                            if (b) {

                                // 秒杀成功给MQ发信息
                                String timeId = IdWorker.getTimeId();
                                SeckillOrderTO seckillOrderTO = new SeckillOrderTO();
                                seckillOrderTO.setOrderSn(timeId);
                                seckillOrderTO.setMemberId(rerspVo.getId());
                                seckillOrderTO.setNum(num);
                                seckillOrderTO.setPromotionSessionId(sessionTo.getPromotionSessionId());
                                seckillOrderTO.setSkuId(sessionTo.getSkuId());
                                seckillOrderTO.setSeckillPrice(sessionTo.getSeckillPrice());
                                rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.queue", seckillOrderTO);
                                return timeId;
                            }
                            return null;


                        } else {

                            return null;
                        }
                    }


                } else {


                    return null;
                }


            } else {

                return null;

            }


        }


        return null;
    }


    private void saveSessionInfos(List<SeckillSessionTo> data) {

        data.stream().forEach(session -> {
            long time = session.getStartTime().getTime();
            long entime = session.getEndTime().getTime();
            String key = SESSIONS_CACHE_PREFIX + time + "_" + entime;
            Boolean aBoolean = redisTemplate.hasKey(key);

            if (!aBoolean) {
                List<String> collect = session.getRelationSkus().stream().map(item -> item.getPromotionSessionId() + "_" + item.getSkuId().toString()).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key, collect);
            }
        });


    }


    private void saveSkuInfos(List<SeckillSessionTo> data) {

        data.stream().forEach(sev -> {
            BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
            sev.getRelationSkus().forEach(item -> {
                //先判断有没有存过，存过就不用再存了
                Boolean isSkuInfo = ops.hasKey(item.getPromotionSessionId().toString() + "_" + item.getSkuId().toString());

                if (isSkuInfo != null && !isSkuInfo) {


                    // 随机码
                    String token = UUID.randomUUID().toString().replace("_", "");
                    Boolean hasKey = ops.hasKey(item.getPromotionSessionId() + "_" + item.getId().toString());

                    if (!hasKey) {
                        SeckillSkuRelationTo seckillSessionTo = new SeckillSkuRelationTo();

                        // sku秒杀信息
                        BeanUtils.copyProperties(item, seckillSessionTo);
                        // 远程查询SKU基本信息
                        R info = productFeinService.getSkuinfo(item.getSkuId());
                        if (info.getCode() == 0) {

                            SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                            });
                            seckillSessionTo.setSkuInfoVo(skuInfo);
                        }
                        // 商品秒杀时间
                        seckillSessionTo.setStartTiem(sev.getStartTime().getTime());
                        seckillSessionTo.setEndTiem(sev.getEndTime().getTime());

                        seckillSessionTo.setRandomCode(token);

                        String key = JSON.toJSONString(seckillSessionTo);

                        ops.put(item.getPromotionSessionId().toString() + "_" + item.getSkuId().toString(), key);
                        // 获取设置信号量
                        RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                        // 设置信号量个数
                        semaphore.trySetPermits(item.getSeckillCount().intValue());
                    }
                }

            });

        });

    }

}
