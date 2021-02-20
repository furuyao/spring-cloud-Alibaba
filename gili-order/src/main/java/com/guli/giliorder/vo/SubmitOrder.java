package com.guli.giliorder.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @创建人: fry
 * @用于： 封装订单提交的数据
 * @创建时间 2020/9/9
 */
@Data
public class SubmitOrder {
    // 收获地址的id
    private Long addrId;
    // 支付方式
    private Integer payType;

    // 防重令牌
    private String orderToken;
    // 订单应付价格
    private BigDecimal payPrice;
    // 用户信息可以在seession取


    // 订单备注
    private String note;

}
