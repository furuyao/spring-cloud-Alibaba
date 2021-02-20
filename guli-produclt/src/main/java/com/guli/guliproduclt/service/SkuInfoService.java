package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.SkuInfoEntity;
import com.guli.guliproduclt.vo.SkuItemVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-19 10:39:18
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
    *@Author: fry
    *@Description: 根据条件查询
    *@Param: [params]
    *@Date: 2020/8/20 10:46
    */
    PageUtils queryPageList(Map<String, Object> params);


    /**
    *@Author: fry
    *@Description: 查询商品详情
    *@Param: [skuId]
    *@Date: 2020/9/1 14:49
    */
    SkuItemVo getSkuItem(Long skuId);



}

