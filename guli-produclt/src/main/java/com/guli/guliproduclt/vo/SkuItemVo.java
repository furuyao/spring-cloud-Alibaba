package com.guli.guliproduclt.vo;

import com.guli.common.to.SeckillSkuRelationTo;
import com.guli.guliproduclt.entity.SkuImagesEntity;
import com.guli.guliproduclt.entity.SkuInfoEntity;
import com.guli.guliproduclt.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/1
 */
@Data
public class SkuItemVo {

    // sku基本信息
    SkuInfoEntity info;

    boolean hasStock= true;

    // 图片信息
    List<SkuImagesEntity> images;

    // spu的销售属性组合
    List<SkuItemSaleAttrVo> saleAttr;

    //spu的介绍
    SpuInfoDescEntity desp;

    // spu的规格参数

    List<SpuItemAttrGroupVo> groupAttrs;

    // 商品秒杀信息
     SeckillSkuRelationTo skuRelationTo;



}
