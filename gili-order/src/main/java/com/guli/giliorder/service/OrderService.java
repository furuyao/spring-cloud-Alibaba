package com.guli.giliorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.to.mq.SeckillOrderTO;
import com.guli.common.utils.PageUtils;
import com.guli.giliorder.config.PayAsyncVo;
import com.guli.giliorder.config.PayVo;
import com.guli.giliorder.entity.OrderEntity;
import com.guli.giliorder.vo.OrderConfirmVo;
import com.guli.giliorder.vo.SubmitOderResponseVo;
import com.guli.giliorder.vo.SubmitOrder;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:28:40
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * @Author: fry
     * @Description: d订单确认也数据
     * @Param: []
     * @Date: 2020/9/8 15:54
     */

    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;


    /**
     * @Author: fry
     * @Description: 提交订单
     * @Param: [order]
     * @Date: 2020/9/9 17:00
     */

    SubmitOderResponseVo submitOrder(SubmitOrder order);

    /**
    *@Author: fry
    *@Description: 查询订单状态
    *@Param: [orderSn]
    *@Date: 2020/9/11 12:42
    */
    OrderEntity getOrderStatus(String orderSn);

    /**
    *@Author: fry
    *@Description: 关闭订单
    *@Param: [orderEntity]
    *@Date: 2020/9/11 16:19
    */
    void cloxseOrder(OrderEntity orderEntity);

    /**
    *@Author: fry
    *@Description: 支付详情
    *@Param: [orderSn]
    *@Date: 2020/9/12 10:26
    */
    PayVo getOrderPay(String orderSn);

    /**
    *@Author: fry
    *@Description: 订单详情查询分页
    *@Param: [params]
    *@Date: 2020/9/12 13:08
    */

    PageUtils listWithItem(Map<String, Object> params);

    /**
    *@Author: fry
    *@Description: 处理支付宝支付结果
    *@Param: [vo]
    *@Date: 2020/9/12 15:15
    */
    String handlePayResult(PayAsyncVo vo);

    /**
    *@Author: fry
    *@Description: 秒杀订单生成
    *@Param: [seckillOrder]
    *@Date: 2020/9/14 12:11
    */
    void createSeckillOrder(SeckillOrderTO seckillOrder);
}

