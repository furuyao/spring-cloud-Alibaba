package com.guli.gulicoupon.service.impl;

import com.guli.common.to.MemberPrice;
import com.guli.common.to.SkuReductionTo;
import com.guli.gulicoupon.entity.MemberPriceEntity;
import com.guli.gulicoupon.entity.SkuLadderEntity;
import com.guli.gulicoupon.service.MemberPriceService;
import com.guli.gulicoupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.gulicoupon.dao.SkuFullReductionDao;
import com.guli.gulicoupon.entity.SkuFullReductionEntity;
import com.guli.gulicoupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceServicel;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {

        // sms_sku_ladder
        SkuLadderEntity skuLadder = new SkuLadderEntity();
        skuLadder.setDiscount(skuReductionTo.getDiscount());
        skuLadder.setSkuId(skuReductionTo.getSkuId());
        skuLadder.setFullCount(skuReductionTo.getFullCount());
        skuLadder.setAddOther(skuReductionTo.getCountStatus());
        if (skuLadder.getFullCount() > 0) {
            skuLadderService.save(skuLadder);
        }

        // sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo, skuFullReductionEntity);
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
            this.save(skuFullReductionEntity);
        }

        // sms_member_price
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> collect = memberPrice.stream().map(itm -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setAddOther(1);
            memberPriceEntity.setMemberLevelId(itm.getId());
            memberPriceEntity.setMemberPrice(itm.getPrice());
            memberPriceEntity.setMemberLevelName(itm.getName());

            return memberPriceEntity;
        }).filter(itm->{

           return  itm.getMemberPrice().compareTo(new BigDecimal("0"))==1;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);

    }

}