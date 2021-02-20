package com.guli.guliproduclt.feign.fallback;

import com.guli.common.exception.BizCodeEnume;
import com.guli.common.utils.R;
import com.guli.guliproduclt.feign.SeckillFeibnService;
import org.springframework.stereotype.Component;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/15
 */
@Component
public class SeckillFeignServiceFallBack implements SeckillFeibnService {
    @Override
    public R skuSeckiIofo(Long skuId) {


        return R.error(BizCodeEnume.TO_MANY_REQUEST.getCode(),BizCodeEnume.TO_MANY_REQUEST.getMsg());
    }
}
