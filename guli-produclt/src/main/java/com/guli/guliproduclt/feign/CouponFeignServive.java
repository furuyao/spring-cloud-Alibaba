package com.guli.guliproduclt.feign;

import com.guli.common.to.SkuReductionTo;
import com.guli.common.to.SpuBoundTo;
import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/19
 */
@FeignClient("guli-coupon")
public interface CouponFeignServive {

    @PostMapping("/spubounds/save")
    R save(@RequestBody SpuBoundTo spuBounds);


    @PostMapping("/skufullreduction/saveSkuReduction")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}


