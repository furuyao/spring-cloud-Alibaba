package com.guli.giliorder.vo;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @创建人: fry
 * @用于： 订单确认页要的数据
 * @创建时间 2020/9/8
 */
public class OrderConfirmVo {

    // 收货地址，
    @Getter
    @Setter
    List<MemberAddressVo> addresses;

    // 所有选中的购物项
    @Getter
    @Setter
    List<OrderItemVo> items;

    @Getter
    @Setter
    private String orderToken; // 防重令牌

    // 优惠卷
    @Getter
    @Setter
    Integer integration;
    // 发票记录

    @Getter
    @Setter
    private Map<Long, Boolean> stocks;


    public Integer getCount() {
        Integer count = 0;
        if (!CollectionUtils.isEmpty(items)) {
            for (OrderItemVo orderItemVO : items)
                count += orderItemVO.getCount();
        }
        return count;
    }

    // private BigDecimal total; // 订单总额
    public BigDecimal getTotal() {
        BigDecimal sum = new BigDecimal("0");
        if (!CollectionUtils.isEmpty(items)) {
            for (OrderItemVo itemVO : items) {
                BigDecimal multiply = itemVO.getPrice().multiply(new BigDecimal(itemVO.getCount().toString()));
                sum = sum.add(multiply);
            }
        }
        return sum;
    }

    // private BigDecimal payPrice; // 应付价格
    public BigDecimal getPayPrice() {
        return getTotal();
    }

}
