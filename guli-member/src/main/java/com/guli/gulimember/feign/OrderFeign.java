package com.guli.gulimember.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/12
 */
@FeignClient("guli-order")
public interface OrderFeign {


    @PostMapping("/order/listWithItem")
    R listWithItem(@RequestBody Map<String, Object> params);
}
