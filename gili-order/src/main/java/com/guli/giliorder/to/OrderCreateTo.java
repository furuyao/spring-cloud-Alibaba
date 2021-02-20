package com.guli.giliorder.to;

import com.guli.giliorder.entity.OrderEntity;
import com.guli.giliorder.entity.OrderItemEntity;
import lombok.Data;
import org.springframework.core.Ordered;

import java.math.BigDecimal;
import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/9
 */
@Data
public class OrderCreateTo {
    // 订单数据
    private OrderEntity order;

    private List<OrderItemEntity> orderItems;

    private BigDecimal payPrice; // 订单的应付价格

    // 运费
    private BigDecimal fare;
}
