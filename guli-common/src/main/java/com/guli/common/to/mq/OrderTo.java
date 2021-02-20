package com.guli.common.to.mq;

import com.guli.common.to.OrderItemVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/11
 */
@Data
public class OrderTo {
    // 订单数据
    private OrderInterceptorTo order;

    private List<OrderItemVo> orderItems;

    private BigDecimal payPrice; // 订单的应付价格

    // 运费
    private BigDecimal fare;

}
