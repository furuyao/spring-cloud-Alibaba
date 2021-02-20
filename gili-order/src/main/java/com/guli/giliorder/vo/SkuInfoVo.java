package com.guli.giliorder.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/9
 */
@Data
public class SkuInfoVo {
    private Long id;
    /**
     * 商品名称
     */
    private String spuName;
    /**
     * 商品描述
     */
    private String spuDescription;
    /**
     * 所属分类id
     */
    private Long catalogId;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     *
     */
    private BigDecimal weight;
    /**
     * 上架状态[0 - 下架，1 - 上架]
     */
    private Integer publishStatus;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;


}
