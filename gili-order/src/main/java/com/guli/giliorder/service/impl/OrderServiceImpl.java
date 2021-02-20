package com.guli.giliorder.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guli.common.constant.OrderStatusEnum;
import com.guli.common.exception.NoStockException;
import com.guli.common.to.mq.OrderInterceptorTo;
import com.guli.common.to.mq.OrderTo;
import com.guli.common.to.mq.SeckillOrderTO;
import com.guli.common.utils.R;
import com.guli.common.vo.MemberRerspVo;
import com.guli.giliorder.config.PayAsyncVo;
import com.guli.giliorder.config.PayVo;
import com.guli.giliorder.constant.OrderConstant;
import com.guli.giliorder.entity.OrderItemEntity;
import com.guli.giliorder.entity.PaymentInfoEntity;
import com.guli.giliorder.feign.CartFenignService;
import com.guli.giliorder.feign.MemberFeignServic;
import com.guli.giliorder.feign.ProductFeignService;
import com.guli.giliorder.feign.WmsFeignService;
import com.guli.giliorder.interceptor.LoginUserInterceptor;
import com.guli.giliorder.service.OrderItemService;
import com.guli.giliorder.service.PaymentInfoService;
import com.guli.giliorder.to.OrderCreateTo;
import com.guli.giliorder.vo.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.giliorder.dao.OrderDao;
import com.guli.giliorder.entity.OrderEntity;
import com.guli.giliorder.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<SubmitOrder> threadLocal = new ThreadLocal<>();
    @Autowired
    OrderDao orderDao;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    MemberFeignServic memberFeignServic;
    @Autowired
    CartFenignService cartFenignService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    WmsFeignService wmsFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    PaymentInfoService paymentInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {

        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberRerspVo memberRerspVo = LoginUserInterceptor.local.get();
        // 由于异步所以ThreadLocal没有用了 就需要单独从主线程拿出来 RequestAttributes拿出来再给每个线程放进去，这样请求头才有
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> async = CompletableFuture.runAsync(() -> {
            //  共享请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            // 远程查询收货地址
            List<MemberAddressVo> address = memberFeignServic.getAddress(memberRerspVo.getId());
            orderConfirmVo.setAddresses(address);


        }, executor);
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            //  共享请求数据
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //远程查询购物车选中的项
            List<OrderItemVo> currentUserCartItems = cartFenignService.getCurrentUserCartItems();
            orderConfirmVo.setItems(currentUserCartItems);


        }, executor).thenRunAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            // 远程查询库存
            List<OrderItemVo> orderItemVos = orderConfirmVo.getItems();
            List<Long> collect = orderItemVos.stream().map(item -> item.getSkuId()).collect(Collectors.toList());
            R skusHasStock = wmsFeignService.getSkusHasStock(collect);
            List<SkuHasStockVo> data = skusHasStock.getData("data", new TypeReference<List<SkuHasStockVo>>() {
            });
            if (data != null) {
                Map<Long, Boolean> map = new HashMap<>();
                for (SkuHasStockVo datum : data) {
                    if (map.put(datum.getSkuId(), datum.getHasStock()) != null) {
                        throw new IllegalStateException("Duplicate key");
                    }
                }
                orderConfirmVo.setStocks(map);
            }


        }, executor);

        // 等待线程完成
        CompletableFuture.allOf(async, voidCompletableFuture).get();
        // 查询用户积分
        Integer integration = memberRerspVo.getIntegration();
        orderConfirmVo.setIntegration(integration);


        //防重令牌
        String replace = UUID.randomUUID().toString().replace("_", "");

        orderConfirmVo.setOrderToken(replace);
        redisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRerspVo.getId(), replace, 30, TimeUnit.MINUTES);


        return orderConfirmVo;
    }

    //    @GlobalTransactional
    @Transactional
    @Override
    public SubmitOderResponseVo submitOrder(SubmitOrder order) {
        threadLocal.set(order);

        SubmitOderResponseVo vo = new SubmitOderResponseVo();
        vo.setCoder(0);
        MemberRerspVo memberRerspVo = LoginUserInterceptor.local.get();
        // 脚本
        String a = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        // 下单

        //验证令牌
        String orderToken = order.getOrderToken();
        // 用脚本删除保证原子性
        Long execute = redisTemplate.execute(new DefaultRedisScript<Long>(a, Long.class), Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRerspVo.getId()), orderToken);

        if (execute == 0) {

            // 验证失败
            return vo;
        } else {
            //创建订单
            OrderCreateTo create = createOrder();
            BigDecimal payAmount = create.getOrder().getPayAmount();
            BigDecimal payPrice = order.getPayPrice();
            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {

                // TODO 验价成功 保存数据
                saveOrder(create);
                // 锁定库存
                WareSkuLokVo lokVo = new WareSkuLokVo();

                lokVo.setOrderSn(create.getOrder().getOrderSn());
                // 要锁定的订单数据
                List<OrderItemVo> collect = create.getOrderItems().stream().map(itm -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(itm.getSkuId());
                    orderItemVo.setCount(itm.getSkuQuantity());
                    orderItemVo.setTitle(itm.getSkuName());
                    return orderItemVo;
                }).collect(Collectors.toList());
                lokVo.setLocks(collect);
                // TODO 远程锁库存
                R r = wmsFeignService.orderLockStock(lokVo);

                if (r.getCode() == 0) {
                    vo.setOrder(create.getOrder());
                    //成功
                    // TODO 发送消息给队列 订单创建成功
                    rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",create.getOrder());


                    return vo;
                } else {
                    //失败
                    String msg = (String) r.get("msg");
                    throw new NoStockException(msg);
                }

            } else {
                vo.setCoder(2);

                return vo;
            }
            // 验证成功
        }


    }

    @Override
    public OrderEntity getOrderStatus(String orderSn) {

        OrderEntity orderEntity = this.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));

        return orderEntity;
    }

    @Override
    public void cloxseOrder(OrderEntity orderEntity) {
        // 先查询订单状态
        OrderEntity entity = this.getById(orderEntity.getId());

        if (entity.getStatus()==OrderStatusEnum.CREAT_NEW.getCoder()){
            OrderEntity orderEntity1 = new OrderEntity();
            orderEntity1.setId(entity.getId());
            orderEntity1.setStatus(OrderStatusEnum.CANCLED.getCoder());

            OrderInterceptorTo orderTo = new OrderInterceptorTo();
            orderTo.setStatus(OrderStatusEnum.CANCLED.getCoder());
            BeanUtils.copyProperties(entity,orderTo);
            this.updateById(orderEntity1);
            // TODO 发信息给队列订单已解绑
            try {
                rabbitTemplate.convertAndSend("order-event-exchange","order.release.other",orderTo);

            }catch (Exception e){
                // TODO 出错重试

            }
        }

    }

    @Override
    public PayVo getOrderPay(String orderSn) {
        PayVo payVo = new PayVo();
        OrderEntity orderStatus = getOrderStatus(orderSn);
        //支付金额向上取值
        BigDecimal decimal = orderStatus.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        payVo.setTotal_amount(decimal.toString());
        // 对外交易号
        payVo.setOut_trade_no(orderStatus.getOrderSn());

        List<OrderItemEntity> order_sn = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderSn));
        OrderItemEntity itemEntity = order_sn.get(0);
        // 设置标题
        payVo.setSubject(itemEntity.getSkuName());
        // 设置备注
        payVo.setBody(itemEntity.getSkuAttrsVals());


        return payVo;
    }

    @Override
    public PageUtils listWithItem(Map<String, Object> params) {
        MemberRerspVo memberRerspVo = LoginUserInterceptor.local.get();

        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>().eq("member_id",memberRerspVo.getId()).orderByDesc("id")
        );
        List<OrderEntity> orderSn = page.getRecords().stream().map(order -> {
            List<OrderItemEntity> order_sn = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", order.getOrderSn()));
            order.setItemEntities(order_sn);

            return order;
        }).collect(Collectors.toList());
        page.setRecords(orderSn);

        return new PageUtils(page);
    }

    @Override
    public String handlePayResult(PayAsyncVo vo) {

        // 保存加以流水
        PaymentInfoEntity infoEntity = new PaymentInfoEntity();


        infoEntity.setAlipayTradeNo(vo.getTrade_no());
        infoEntity.setOrderSn(vo.getOut_trade_no());
        infoEntity.setPaymentStatus(vo.getTrade_status());
        infoEntity.setCallbackTime(vo.getNotify_time());
        paymentInfoService.save(infoEntity);
        if (vo.getTrade_status().equals("TRADE_SUCCESS") || vo.getTrade_status().equals("TRADE_FINISHED")){

            String outTradeNo = vo.getOut_trade_no();

            this.baseMapper.updateOrderpayedStatus(outTradeNo,OrderStatusEnum.PAYED.getCoder());


        }


        return "success";
    }

    @Override
    public void createSeckillOrder(SeckillOrderTO seckillOrder) {
        // TODO 保存完整的订单信息 现在只是个简单的占位方法
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(seckillOrder.getOrderSn());
        orderEntity.setMemberId(seckillOrder.getMemberId());
        orderEntity.setStatus(OrderStatusEnum.CREAT_NEW.getCoder());
        BigDecimal result = seckillOrder.getSeckillPrice().multiply(new BigDecimal("" + seckillOrder.getNum()));
        orderEntity.setPayAmount(result);
        this.save(orderEntity);

        // TODO 保存订单项信息
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        orderItemEntity.setOrderSn(seckillOrder.getOrderSn());
        orderItemEntity.setRealAmount(result);

        // TODO 获取当前SKU的详细信息进行设置 productFeign.getSpuInfoBySkuId()
        orderItemEntity.setSkuQuantity(seckillOrder.getNum());

        orderItemService.save(orderItemEntity);




    }


    /**
     * @Author: fry
     * @Description: 保存订单数据
     * @Param: [create]
     * @Date: 2020/9/10 11:00
     */
    private void saveOrder(OrderCreateTo create) {
        OrderEntity order = create.getOrder();
        order.setModifyTime(new Date());
        this.save(order);

        List<OrderItemEntity> orderItems = create.getOrderItems();
        orderItemService.saveBatch(orderItems);

    }


    private OrderCreateTo createOrder() {
        OrderCreateTo create = new OrderCreateTo();
        // 订单ID
        String timeId = IdWorker.getTimeId();
        // 订单数据
        OrderEntity orderEntity = buildOrder(timeId);
        // 获取所有订单项
        List<OrderItemEntity> orderItemEntities = buildOrderItem(timeId);
        // 验价
        computePrice(orderEntity, orderItemEntities);
        create.setOrder(orderEntity);
        create.setOrderItems(orderItemEntities);
        return create;
    }

    /**
     * @Author: fry
     * @Description: 验价计算
     * @Param: [orderEntity]
     * @Date: 2020/9/9 21:05
     */
    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {

        BigDecimal decimal = new BigDecimal("0.0");
        //           优惠券优惠
        BigDecimal coupuo = new BigDecimal("0.0");
        //            积分优惠
        BigDecimal coupu = new BigDecimal("0.0");
        //            商品促销
        BigDecimal coup = new BigDecimal("0.0");
        // 可获得积分
        BigDecimal integer = new BigDecimal("0.0");
        // 可获得成长值
        BigDecimal growth = new BigDecimal("0.0");


        for (OrderItemEntity itemEntity : orderItemEntities) {
            BigDecimal decfr = itemEntity.getRealAmount();

            coupuo = coupuo.add(itemEntity.getCouponAmount());

            coupu = coupu.add(itemEntity.getIntegrationAmount());

            coup = coup.add(itemEntity.getPromotionAmount());

            integer.add(new BigDecimal(itemEntity.getGiftIntegration().toString()));

            growth.add(new BigDecimal(itemEntity.getGiftGrowth().toString()));


            decimal = decimal.add(decfr);

        }


        // 订单价格相关
        orderEntity.setTotalAmount(decimal);

        // 应付总额
        orderEntity.setPayAmount(decimal.add(orderEntity.getFreightAmount()));

        orderEntity.setIntegrationAmount(coupu);
        // 商品总优惠
        orderEntity.setPromotionAmount(coup);

        orderEntity.setCouponAmount(coupuo);
        // 设置可获得积分
        orderEntity.setIntegration(integer.intValue());
        // 设置可获得成长值
        orderEntity.setGrowth(growth.intValue());

        // 删除状态 0是为删除
        orderEntity.setDeleteStatus(0);
    }


    /**
     * @Author: fry
     * @Description: 构建订单
     * @Param: [submitOrder, orderEntity]
     * @Date: 2020/9/9 20:08
     */
    private OrderEntity buildOrder(String timeId) {
        MemberRerspVo memberRerspVo = LoginUserInterceptor.local.get();
        OrderEntity orderEntity = new OrderEntity();

        SubmitOrder submitOrder = threadLocal.get();

        orderEntity.setOrderSn(timeId);
        //远程查询邮费和收费信息

        // 会员id
        orderEntity.setMemberId(memberRerspVo.getId());

        R fare = wmsFeignService.getFare(submitOrder.getAddrId());

        FareVo data = fare.getData(new TypeReference<FareVo>() {
        });
        // 运费
        orderEntity.setFreightAmount(data.getFare());
        // 收货人信息
        orderEntity.setReceiverCity(data.getAddress().getCity());
        orderEntity.setReceiverDetailAddress(data.getAddress().getDetailAddress());
        orderEntity.setReceiverName(data.getAddress().getName());
        orderEntity.setReceiverPhone(data.getAddress().getPhone());
        orderEntity.setReceiverPostCode(data.getAddress().getPostCode());
        orderEntity.setReceiverProvince(data.getAddress().getProvince());
        orderEntity.setReceiverRegion(data.getAddress().getRegion());

        // 订单状态
        orderEntity.setStatus(OrderStatusEnum.CREAT_NEW.getCoder());
        orderEntity.setAutoConfirmDay(7);

        return orderEntity;
    }

    /**
     * @Author: fry
     * @Description: 构建全都订单项
     * @Param: []
     * @Date: 2020/9/9 20:06
     */
    private List<OrderItemEntity> buildOrderItem(String timeId) {

        // 获取所有订单项
        // 最后确定价格
        List<OrderItemVo> currentUserCartItems = cartFenignService.getCurrentUserCartItems();
        if (currentUserCartItems != null && currentUserCartItems.size() > 0) {

            List<OrderItemEntity> collect = currentUserCartItems.stream().map((item) -> {
                // 构建订单项
                OrderItemEntity orderItemEntity = bidedOrderItem(item, timeId);


                return orderItemEntity;

            }).collect(Collectors.toList());

            return collect;
        }


        return null;
    }

    /**
     * @Author: fry
     * @Description: 构建某一个订单项
     * @Param: []
     * @Date: 2020/9/9 20:16
     */
    private OrderItemEntity bidedOrderItem(OrderItemVo item, String timeId) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        // 订单Id
        orderItemEntity.setOrderSn(timeId);
        // 商品sku信息
        orderItemEntity.setSkuId(item.getSkuId());
        orderItemEntity.setSkuName(item.getTitle());
        orderItemEntity.setSkuPic(item.getImage());
        orderItemEntity.setSkuPrice(item.getPrice());
        String key = StringUtils.collectionToDelimitedString(item.getSkuAttr(), ";");
        orderItemEntity.setSkuAttrsVals(key);
        orderItemEntity.setSkuQuantity(item.getCount());
        // spu信息
        R spuInfoById = productFeignService.getSpuInfoById(item.getSkuId());
        SkuInfoVo data = spuInfoById.getData(new TypeReference<SkuInfoVo>() {
        });
        orderItemEntity.setSpuId(data.getId());
        orderItemEntity.setSpuBrand(data.getBrandId().toString());
        orderItemEntity.setSpuName(data.getSpuName());
        orderItemEntity.setCategoryId(data.getCatalogId());
        // 积分信息
        orderItemEntity.setGiftGrowth(item.getPrice().multiply(new BigDecimal(data.getCatalogId().toString())).intValue());
        orderItemEntity.setGiftIntegration(item.getPrice().multiply(new BigDecimal(data.getCatalogId().toString())).intValue());
        //订单价格
        orderItemEntity.setPromotionAmount(new BigDecimal("0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0"));
        // 原来的价格
        BigDecimal multiply = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        BigDecimal subtract = multiply.subtract(orderItemEntity.getIntegrationAmount()).subtract(orderItemEntity.getCouponAmount()).subtract(orderItemEntity.getPromotionAmount());
        // 当前订单实际金额
        orderItemEntity.setRealAmount(subtract);

        return orderItemEntity;
    }


}