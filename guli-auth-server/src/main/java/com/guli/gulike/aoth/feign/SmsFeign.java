package com.guli.gulike.aoth.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/3
 */
@FeignClient("guli-third-party")
public interface SmsFeign {

    @GetMapping("/sms/component")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

}
