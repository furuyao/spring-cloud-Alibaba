package com.guli.guliproduclt.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.guli.common.to.SeckillSkuRelationTo;
import com.guli.common.utils.R;
import com.guli.guliproduclt.dao.SkuInfoDao;
import com.guli.guliproduclt.entity.SkuImagesEntity;
import com.guli.guliproduclt.entity.SkuInfoEntity;
import com.guli.guliproduclt.entity.SpuInfoDescEntity;
import com.guli.guliproduclt.feign.SeckillFeibnService;
import com.guli.guliproduclt.service.*;
import com.guli.guliproduclt.vo.SkuItemSaleAttrVo;
import com.guli.guliproduclt.vo.SkuItemVo;
import com.guli.guliproduclt.vo.SpuItemAttrGroupVo;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {


    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    AttrGroupService attrGroupService;


    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    SeckillFeibnService seckillFeibnService;

    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageList(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> Wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            Wrapper.and(itm -> {
                itm.eq("id", key).or().eq("sku_name", key);

            });

        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            Wrapper.eq("catalog_id", catelogId);

        }
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            Wrapper.eq("brand_id", brandId);

        }
        String min = (String) params.get("min");
        if (StringUtils.isNotEmpty(min)) {
            Wrapper.ge("price", min);

        }
        String max = (String) params.get("max");
        if (StringUtils.isNotEmpty(max) && !"0".equalsIgnoreCase(max)) {
            Wrapper.le("price", max);

        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                Wrapper
        );

        return new PageUtils(page);
    }

    @SneakyThrows
    @Override
    public SkuItemVo getSkuItem(Long skuId) {

        SkuItemVo skuItemVo = new SkuItemVo();

        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            // 1,SKU查询基本信息
            SkuInfoEntity byId = this.getById(skuId);
            skuItemVo.setInfo(byId);
            return byId;
        }, executor);


        CompletableFuture<Void> attrvo = infoFuture.thenAcceptAsync((ren) -> {
            // 3,SPU的销售属性组合
            List<SkuItemSaleAttrVo> saleAttrBySpuId = skuSaleAttrValueService.getSaleAttrBySpuId(ren.getSpuId());

            skuItemVo.setSaleAttr(saleAttrBySpuId);

        }, executor);

        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((ren) -> {

            // 4,spu的介绍

            SpuInfoDescEntity byId1 = spuInfoDescService.getById(ren.getSpuId());
            skuItemVo.setDesp(byId1);


        }, executor);

        CompletableFuture<Void> groupAsync = infoFuture.thenAcceptAsync((ren) -> {
            // 5,spu的规格参数
            List<SpuItemAttrGroupVo> group = attrGroupService.getAttrGroupWithAttrsBySpuId(skuId, ren.getCatalogId());
            skuItemVo.setGroupAttrs(group);

        }, executor);


        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            // 2,SKU的图片信息
            List<SkuImagesEntity> skuList = skuImagesService.list(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId));
            skuItemVo.setImages(skuList);
        }, executor);


        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {

            R r = seckillFeibnService.skuSeckiIofo(skuId);
            if (r.getCode() == 0 && r.get("data") !=null) {

                SeckillSkuRelationTo data = r.getData("data", new TypeReference<SeckillSkuRelationTo>() {
                });
                skuItemVo.setSkuRelationTo(data);
            }

        }, executor);


        // 等待任务完成
        CompletableFuture.allOf(attrvo,descFuture,groupAsync,imagesFuture,completableFuture).get();
        // 查询当前Sku秒杀信息


        return skuItemVo;
    }



}