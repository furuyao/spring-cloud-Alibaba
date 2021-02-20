package com.guli.gulike.secki.service;

import com.guli.common.to.SeckillSessionTo;
import com.guli.common.to.SeckillSkuRelationTo;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
public interface SeckillService {

    /***
    *@Author: fry
    *@Description: 秒杀上架
    *@Param: []
    *@Date: 2020/9/13 11:24
    */

    void updeleadSecKilSkuLatest3Days();

    /**
    *@Author: fry
    *@Description: 查询秒杀商品
    *@Param: []
    *@Date: 2020/9/13 16:48
    */
    List<SeckillSkuRelationTo> getCurrentSeckillSkus();

    
    SeckillSkuRelationTo getSkuSeckillInfo(Long skuId);

    /**
    *@Author: fry
    *@Description: 秒杀
    *@Param: [killId, key, num]
    *@Date: 2020/9/14 11:01
    */
    String secKill(String killId, String key, Integer num);
}
