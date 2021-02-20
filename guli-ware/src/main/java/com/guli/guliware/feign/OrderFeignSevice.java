package com.guli.guliware.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/11
 */
@FeignClient("guli-order")
public interface OrderFeignSevice {
     @GetMapping("/order/status/{orderSn}")
     R getOrderStatus(@PathVariable("orderSn") String orderSn);
}
