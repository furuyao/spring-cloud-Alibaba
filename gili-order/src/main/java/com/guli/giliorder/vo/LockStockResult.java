package com.guli.giliorder.vo;

import lombok.Data;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/10
 */
@Data
public class LockStockResult {

    private Long skuId;

    private Integer num;

    private Boolean locked;


}
