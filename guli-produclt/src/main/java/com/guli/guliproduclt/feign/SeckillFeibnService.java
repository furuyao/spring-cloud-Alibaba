package com.guli.guliproduclt.feign;

import com.guli.common.utils.R;
import com.guli.guliproduclt.feign.fallback.SeckillFeignServiceFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
@FeignClient(value = "guli-secki", fallback = SeckillFeignServiceFallBack.class)
public interface SeckillFeibnService {

    @GetMapping("/sku/seckill/{skuId}")
    R skuSeckiIofo(@PathVariable("skuId") Long skuId);

}
