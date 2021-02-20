package com.guli.guliware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.to.mq.OrderInterceptorTo;
import com.guli.common.to.mq.StockLockedTo;
import com.guli.common.utils.PageUtils;
import com.guli.guliware.entity.WareSkuEntity;
import com.guli.common.to.SkuHasStockVo;
import com.guli.guliware.vo.WareSkuLokVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:33:31
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
    *@Author: fry
    *@Description: 查询库存
    *@Param: [skuId]
    *@Date: 2020/8/24 19:40
    */
    List<SkuHasStockVo> getSkusHasStock(List<Long> skuId);

    /**
    *@Author: fry
    *@Description: 锁定库存返回结果
    *@Param: [wareSkuLokVo]
    *@Date: 2020/9/10 11:20
    */
    Boolean orderLockStock(WareSkuLokVo wareSkuLokVo);

    /**
     * 解锁库存
     * @param stockLockedTo
     */
    void unlocStock(StockLockedTo stockLockedTo);

    /**
     * 主动解锁库存
     * @param stockLockedTo
     */
    void unlocOrderRelease(OrderInterceptorTo stockLockedTo);
}

