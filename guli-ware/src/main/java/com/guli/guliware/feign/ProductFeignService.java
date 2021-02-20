package com.guli.guliware.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/21
 */
@FeignClient("guli-produclt")
public interface ProductFeignService {

    /**
    *@Author: fry
    *@Description: id远程查询商品信息
    *@Param: [skuId]
    *@Date: 2020/8/21 13:03
    */
    @RequestMapping("/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);



}
