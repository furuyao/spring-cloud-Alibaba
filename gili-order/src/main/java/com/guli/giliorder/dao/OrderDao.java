package com.guli.giliorder.dao;

import com.guli.giliorder.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:28:40
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    void updateOrderpayedStatus(@Param("outTradeNo") String outTradeNo, @Param("coder") int coder);
}
