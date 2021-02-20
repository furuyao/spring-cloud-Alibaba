package com.guli.giliorder.feign;

import com.guli.common.utils.R;
import com.guli.giliorder.vo.WareSkuLokVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @创建人: fry
 * @用于：库存系统接口
 * @创建时间 2020/9/9
 */
@FeignClient("guli-ware")
public interface WmsFeignService {

    /**
     * 查询是否有库存
     */
    @PostMapping("/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuId);


    @GetMapping("/wareinfo/fare")
    R getFare(@RequestParam("addrId") Long addrId);

    @PostMapping("/waresku/lock/order")
    R orderLockStock(@RequestBody WareSkuLokVo wareSkuLokVo);
}
