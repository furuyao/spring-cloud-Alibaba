package com.guli.guliware.feign;

import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/9
 */
@FeignClient("guli-member")
public interface MemberFeignService {

    /**
     * id查询收货地址
     */
    @RequestMapping("/memberreceiveaddress/info/{id}")
    R info(@PathVariable("id") Long id);

}
