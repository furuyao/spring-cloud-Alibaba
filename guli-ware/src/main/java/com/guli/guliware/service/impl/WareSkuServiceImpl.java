package com.guli.guliware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.guli.common.exception.NoStockException;
import com.guli.common.to.mq.OrderInterceptorTo;
import com.guli.common.to.mq.StockDetailTo;
import com.guli.common.to.mq.StockLockedTo;
import com.guli.common.utils.R;
import com.guli.guliware.entity.WareOrderTaskDetailEntity;
import com.guli.guliware.entity.WareOrderTaskEntity;
import com.guli.guliware.feign.OrderFeignSevice;
import com.guli.guliware.feign.ProductFeignService;
import com.guli.common.to.SkuHasStockVo;
import com.guli.guliware.service.WareOrderTaskDetailService;
import com.guli.guliware.service.WareOrderTaskService;
import com.guli.guliware.vo.OrderItemVo;
import com.guli.guliware.vo.OrderVo;
import com.guli.guliware.vo.WareSkuLokVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.guliware.dao.WareSkuDao;
import com.guli.guliware.entity.WareSkuEntity;
import com.guli.guliware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    WareSkuDao wareSkuDao;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Autowired
    OrderFeignSevice orderFeignSevice;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();

        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("ware_id", wareId);

        }
        String skuId = (String) params.get("skuId");

        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq("sku_id", skuId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {

        List<WareSkuEntity> wareSkuEntities = this.baseMapper.selectList(
                new QueryWrapper<WareSkuEntity>().eq("ware_id", wareId).eq("sku_id", skuId));

        if (wareSkuEntities.size() > 0 || wareSkuEntities != null) {
            this.baseMapper.addStock(skuId, wareId, skuNum);
        } else {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);

            try {
                R info = productFeignService.info(skuId);

                Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));

            } catch (Exception e) {

                log.info("远程调用查询SKUn amen失败" + e);
            }

            wareSkuEntity.setStock(0);
            this.baseMapper.insert(wareSkuEntity);

        }

    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuId) {

        List<SkuHasStockVo> collect = skuId.stream().map(itm -> {

            SkuHasStockVo vo = new SkuHasStockVo();

            Long count = baseMapper.getSkusHasStock(itm);
            vo.setSkuId(itm);
            vo.setHasStock(count == null ? false : count > 0);
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    /**
     * (rollbackFor = NoStockException.class)
     * 默认运行时异常都回滚
     *
     * @param wareSkuLokVo
     * @return
     */


    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLokVo wareSkuLokVo) {


        /**
         * 保存库存工作单，方便人为回滚和追溯
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(wareSkuLokVo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);


        List<OrderItemVo> locks = wareSkuLokVo.getLocks();
        // 查询那些厂库有有库存
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock skuWareHasStock = new SkuWareHasStock();
            skuWareHasStock.setNum(item.getCount());
            skuWareHasStock.setSkuId(item.getSkuId());
            List<Long> list = this.baseMapper.selectWareId(item.getSkuId());
            skuWareHasStock.setWareId(list);

            return skuWareHasStock;
        }).collect(Collectors.toList());
        Boolean aBoolean = true;


        // 锁定库存
        for (SkuWareHasStock war : collect) {

            Boolean skuStocked = false;
            Long skuId = war.getSkuId();
            List<Long> wareId = war.getWareId();
            if (wareId == null || wareId.size() == 0) {
                // 没有一个仓库有库存
                throw new NoStockException(skuId.toString());
            }

            for (Long aLong : wareId) {

                //扣库存
                Long cont = wareSkuDao.lockSkuStock(skuId, aLong, war.getNum());
                if (cont == 1) {
                    skuStocked = true;
                    WareOrderTaskDetailEntity detailEntity = new WareOrderTaskDetailEntity(null, skuId, null, war.getNum(), wareOrderTaskEntity.getId(), aLong, 1);
                    wareOrderTaskDetailService.save(detailEntity);

                    //  锁成功 发信息给队列通知库存锁定成功
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(detailEntity, stockDetailTo);
                    stockLockedTo.setDetail(stockDetailTo);
                    stockLockedTo.setId(wareOrderTaskEntity.getId());
                    System.out.println("--------------------订单锁定成功---------------");
                    rabbitTemplate.convertAndSend("stock-event-exchange", "stock.locked", stockLockedTo);

                    break;
                }
            }
            if (skuStocked == false) {
                throw new NoStockException(skuId.toString());
            }

        }

        // 全部锁定成功
        return true;
    }

    @Override
    public void unlocStock(StockLockedTo stockLockedTo) {

        StockDetailTo detail = stockLockedTo.getDetail();
        // 工作单ID
        Long id = stockLockedTo.getId();
        //  解锁项ID
        Long detailId = detail.getId();
        WareOrderTaskEntity taskServiceById = wareOrderTaskService.getById(id);
        // 解锁:
        // 1、查询数据库关于这个订单的锁定库存信息。
        // 1。有:这个订单项是锁定成功了的
//                解锁：
//                     1.没有这个订单，不用解锁
//                     2.支付了，不用解锁
//                     3.订单状态已取消：解锁
//                     4.没有取消并且没支付，解锁  4->已关闭；5->无效订单  0->待付款
        //  1没有:库存锁定失败了，库存回滚了。这种情况无需解锁
        WareOrderTaskDetailEntity byId = wareOrderTaskDetailService.getById(detailId);
        if (byId != null) {
            // 远程获取订单状态
            R orderStatus = orderFeignSevice.getOrderStatus(taskServiceById.getOrderSn());
            if (orderStatus.getCode() == 0) {

                OrderVo data = orderStatus.getData(new TypeReference<OrderVo>() {
                });
                if (data == null || data.getStatus() == 4 ) {
                    if (byId.getLockStatus()==1){

                        // 解锁
                        unLokStock(detail.getSkuId(), detail.getSkuNum(), detail.getWareId(), detailId);

                    }

                }
            }else {

                // 远程调用失败应该抛异常 消息回到队列
                throw  new RuntimeException("远程调用失败");

            }
        }else {

            //无需解锁

        }

    }
    @Transactional
    @Override
    public void unlocOrderRelease(OrderInterceptorTo stockLockedTo) {
            // 防止订单系统卡，导致库存先解锁了库存
        String orderSn = stockLockedTo.getOrderSn();
        // 查已下最新数据查看最新状态
        WareOrderTaskEntity order_sn = wareOrderTaskService.getOne(new QueryWrapper<WareOrderTaskEntity>().eq("order_sn", orderSn));
        List<WareOrderTaskDetailEntity> list = wareOrderTaskDetailService.list(
                new QueryWrapper<WareOrderTaskDetailEntity>().eq("task_id", order_sn.getId()).eq("lock_status",1));

        for (WareOrderTaskDetailEntity entity:list){
            unLokStock(entity.getSkuId(),entity.getSkuNum(),entity.getWareId(),entity.getId());
        }
    }

    @Data
    class SkuWareHasStock {
        private Long skuId;

        private Integer num;

        private List<Long> wareId;


    }


    /**
     * 解锁方法
     *
     * @param skuId    商品ID
     * @param skuNum   解锁数量lock_status
     * @param wareId   仓库ID
     */
    private void unLokStock(Long skuId, Integer skuNum, Long wareId, Long detailId) {
        this.getBaseMapper().unlockStock(skuId, skuNum, wareId);

        WareOrderTaskDetailEntity wareOrder = new WareOrderTaskDetailEntity();
        wareOrder.setId(detailId);
        wareOrder.setLockStatus(2);
        wareOrderTaskDetailService.updateById(wareOrder);

    }


}