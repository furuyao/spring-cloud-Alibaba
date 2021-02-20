package com.guli.giliorder.vo;

import lombok.Data;

import java.util.List;

/**
 * @创建人: fry
 * @用于： 远程锁库存
 * @创建时间 2020/9/10
 */
@Data
public class WareSkuLokVo {

    private String orderSn;

    private List<OrderItemVo> locks ;



}
