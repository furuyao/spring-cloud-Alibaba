package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.SkuSaleAttrValueEntity;
import com.guli.guliproduclt.vo.SkuItemSaleAttrVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-19 10:39:18
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
    *@Author: fry
    *@Description: SPU的销售属性组合
    *@Param: [spuId]
    *@Date: 2020/9/1 18:47
    */
    List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId);

    List<String> getSkuSaleAttrValues(Long skuId);
}

