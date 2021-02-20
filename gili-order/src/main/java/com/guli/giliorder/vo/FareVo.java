package com.guli.giliorder.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/9
 */
@Data
public class FareVo {
        // 地址
        private MemberAddressVo address;
        // 运费
       private BigDecimal fare;


}
