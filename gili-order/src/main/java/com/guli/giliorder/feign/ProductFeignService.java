package com.guli.giliorder.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/9
 */

@FeignClient("guli-produclt")
public interface ProductFeignService {

    @GetMapping("/spuinfo/skuId/{skuId}")
    R getSpuInfoById(@PathVariable("skuId") Long skuId);


}
