package com.guli.giliorder.feign;

import com.guli.giliorder.vo.MemberAddressVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/8
 */
@FeignClient("guli-member")
public interface MemberFeignServic {

    @GetMapping("/memberreceiveaddress/{memeberId}/addresses")
    List<MemberAddressVo> getAddress(@PathVariable("memeberId") Long memeberId);



}
