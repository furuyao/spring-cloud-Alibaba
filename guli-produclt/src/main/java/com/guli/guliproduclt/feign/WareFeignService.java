package com.guli.guliproduclt.feign;

import com.guli.common.to.SkuHasStockVo;
import com.guli.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/24
 */
@FeignClient("guli-ware")
public interface WareFeignService {



    @PostMapping("/waresku/hasstock")
    R getSkusHasStock(@RequestBody List<Long> skuId);

}
