package com.guli.giliorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.giliorder.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:28:40
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

