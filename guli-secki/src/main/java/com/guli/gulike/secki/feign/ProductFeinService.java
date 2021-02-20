package com.guli.gulike.secki.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
@FeignClient("guli-produclt")
public interface ProductFeinService {
    /**
    *@Author: fry
    *@Description: 远程查询商品信息
    *@Param: [skuId]
    *@Date: 2020/9/13 14:30
    */

    @RequestMapping("/skuinfo/info/{skuId}")
    R getSkuinfo(@PathVariable("skuId") Long skuId);

}
