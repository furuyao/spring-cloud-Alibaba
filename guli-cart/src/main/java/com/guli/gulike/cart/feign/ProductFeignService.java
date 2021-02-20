package com.guli.gulike.cart.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/6
 */
@FeignClient("guli-produclt")
public interface ProductFeignService {

    /**
     * 查询商品信息
     */
    @RequestMapping("/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);

    /**
     * 查询销售信息
     * @param skuId
     * @return
     */
    @GetMapping("/skusaleattrvalue/stringlist/{skuId}")
    List<String> getSkuSaleAttrValues(@PathVariable("skuId") Long skuId);

    /**
     * 获取价格
     * @param skuId
     * @return
     */
    @GetMapping("/skuinfo/{skuId}/price")
    R getPrice(@PathVariable("skuId") Long skuId);

}
