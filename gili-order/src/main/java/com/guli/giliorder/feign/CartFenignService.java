package com.guli.giliorder.feign;

import com.guli.giliorder.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/8
 */
@FeignClient("guli-cart")
public interface CartFenignService {

    @GetMapping("/currentUserCartItems")
    List<OrderItemVo> getCurrentUserCartItems();
}
