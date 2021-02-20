package com.guli.gulicoupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.to.SkuReductionTo;
import com.guli.common.utils.PageUtils;
import com.guli.gulicoupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 11:14:23
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);


    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

