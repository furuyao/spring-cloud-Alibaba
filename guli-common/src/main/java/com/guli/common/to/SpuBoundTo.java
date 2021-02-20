package com.guli.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/19
 */
@Data
public class SpuBoundTo {

    private Long  spuId;

    private BigDecimal buyBounds;

    private BigDecimal growBounds;
}
