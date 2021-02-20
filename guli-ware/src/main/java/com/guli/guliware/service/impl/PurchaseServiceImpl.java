package com.guli.guliware.service.impl;

import com.guli.common.constant.WareConstant;
import com.guli.guliware.entity.PurchaseDetailEntity;
import com.guli.guliware.service.PurchaseDetailService;
import com.guli.guliware.service.WareSkuService;
import com.guli.guliware.vo.MergeVo;
import com.guli.guliware.vo.PurchaseDoneVo;
import com.guli.guliware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.guliware.dao.PurchaseDao;
import com.guli.guliware.entity.PurchaseEntity;
import com.guli.guliware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPagePurchase(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 1).or().eq("status", 0)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void queryPageMerge(MergeVo params) {
        Long purchaseId = params.getPurchaseId();

        if (purchaseId == 0) {
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseEnum.CREATED.getCode());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setCreateTime(new Date());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        List<Long> items = params.getItems();

        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(itm -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(itm);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDateEnum.ASSIGNED.getCode());

            return purchaseDetailEntity;
        }).collect(Collectors.toList());

        purchaseDetailService.updateBatchById(collect);

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);


    }

    @Override
    public void received(List<Long> ids) {

        List<PurchaseEntity> collect = ids.stream().map(itm -> {

            PurchaseEntity byId = this.getById(itm);
            return byId;
        }).filter(itms -> {
            if (itms.getStatus() == WareConstant.PurchaseEnum.CREATED.getCode() ||
                    itms.getStatus() == WareConstant.PurchaseEnum.ASSIGNED.getCode()) {
                return true;
            } else {
                return false;
            }
        }).map(itmo -> {
            itmo.setStatus((WareConstant.PurchaseEnum.RECEIVE.getCode()));
            itmo.setUpdateTime(new Date());
            return itmo;
        }).collect(Collectors.toList());
        // 改变采购单状态
        this.updateBatchById(collect);

        // 改变采购项的装态
        collect.stream().forEach(ity -> {
            List<PurchaseDetailEntity> detailEntities = purchaseDetailService.getListChaseId(ity.getId());
            List<PurchaseDetailEntity> collect1 = detailEntities.stream().map(itm -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(itm.getId());
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDateEnum.BUYING.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);
        });


    }
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {

        Long id = doneVo.getId();

        List<PurchaseDetailEntity> detailEntities = new ArrayList<>();

        Boolean aBoolean = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();
        for (PurchaseItemDoneVo vo : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (vo.getStatus() == WareConstant.PurchaseEnum.HASERROR.getCode()) {
                aBoolean = false;
                detailEntity.setStatus(vo.getStatus());
            } else {
                detailEntity.setStatus(WareConstant.PurchaseEnum.FINISH.getCode());

                PurchaseDetailEntity byId = purchaseDetailService.getById(vo.getItemId());
                // 采购商品入库
                wareSkuService.addStock(byId.getSkuId(),byId.getWareId(),byId.getSkuNum());
            }
            detailEntity.setId(vo.getItemId());
            detailEntities.add(detailEntity);
        }
        // 跟新订单项
        purchaseDetailService.updateBatchById(detailEntities);

        // 改变订单装态

        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(aBoolean ? WareConstant.PurchaseEnum.FINISH.getCode() : WareConstant.PurchaseEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);


    }

}