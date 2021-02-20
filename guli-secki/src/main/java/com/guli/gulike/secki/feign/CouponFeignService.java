package com.guli.gulike.secki.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
@FeignClient("guli-coupon")
public interface CouponFeignService {

    @GetMapping("/seckillsession/lates3DaySession")
    R getKilSkuLatest3Days();

}
