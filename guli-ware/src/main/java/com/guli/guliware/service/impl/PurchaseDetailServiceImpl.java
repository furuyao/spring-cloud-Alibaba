package com.guli.guliware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.guliware.dao.PurchaseDetailDao;
import com.guli.guliware.entity.PurchaseDetailEntity;
import com.guli.guliware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();

       String key= (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and(itm->{
                itm.eq("id",key).or().eq("sku_price",key);

            });
        }
        String status= (String) params.get("status");
        if (!StringUtils.isEmpty(status)){

            queryWrapper.eq("status",status);
        }
        String wareId= (String) params.get("wareId");
        if (!StringUtils.isEmpty(status)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> getListChaseId(Long id) {

        List<PurchaseDetailEntity> list = this.list(new QueryWrapper<PurchaseDetailEntity>()
                .eq("purchase_id", id));

        return list;
    }

}