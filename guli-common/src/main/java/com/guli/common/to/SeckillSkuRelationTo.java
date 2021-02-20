package com.guli.common.to;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
@Data
public class SeckillSkuRelationTo {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 活动id
     */
    private Long promotionId;
    /**
     * 活动场次id
     */
    private Long promotionSessionId;
    /**
     * 商品id
     */
    private Long skuId;

    /**
     * 秒杀随机码
     */
    private String randomCode;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 秒杀总量
     */
    private BigDecimal seckillCount;
    /**
     * 每人限购数量
     */
    private BigDecimal seckillLimit;
    /**
     * 排序
     */
    private Integer seckillSort;

    /**
     * 商品详情
     */
    private SkuInfoVo skuInfoVo;





    /**
     * 开始时间
     */
    private Long startTiem;

    /**
     * 结束时间
     */
    private Long endTiem;

}
