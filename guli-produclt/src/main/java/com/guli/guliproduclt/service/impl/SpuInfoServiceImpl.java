package com.guli.guliproduclt.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.guli.common.constant.ProductConstant;
import com.guli.common.to.SkuEsModel;
import com.guli.common.to.SkuHasStockVo;
import com.guli.common.to.SkuReductionTo;
import com.guli.common.to.SpuBoundTo;
import com.guli.common.utils.R;
import com.guli.guliproduclt.entity.*;
import com.guli.guliproduclt.feign.CouponFeignServive;
import com.guli.guliproduclt.feign.SearchFeignService;
import com.guli.guliproduclt.feign.WareFeignService;
import com.guli.guliproduclt.service.*;
import com.guli.guliproduclt.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.guliproduclt.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignServive couponFeignServive;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuinfo(SpuSaveVo spuInfo) {
        // 保存基本数据 pms_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        BeanUtils.copyProperties(spuInfo, infoEntity);
        this.save(infoEntity);
        // 保存描述图片  pms_spu_info_desc

        List<String> decript = spuInfo.getDecript();
        SpuInfoDescEntity spuInfoDesc = new SpuInfoDescEntity();
        spuInfoDesc.setSpuId(infoEntity.getId());
        spuInfoDesc.setDecript(String.join(",", decript));
        this.saveInfoDecript(spuInfoDesc);

        // 保存图片集 pms_spu_images
        spuImagesService.saveList(infoEntity.getId(), spuInfo.getImages());

        // 保存规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValue = new ProductAttrValueEntity();
            productAttrValue.setAttrId(attr.getAttrId());
            AttrEntity byId = attrService.getById(attr.getAttrId());
            productAttrValue.setAttrName(byId.getAttrName());
            productAttrValue.setAttrValue(attr.getAttrValues());
            productAttrValue.setAttrSort(attr.getShowDesc());
            productAttrValue.setSpuId(infoEntity.getId());
            return productAttrValue;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(collect);
        // 保存积分信息  (跨库)
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        Bounds bounds = spuInfo.getBounds();
        BeanUtils.copyProperties(bounds, spuBoundTo);
        spuBoundTo.setSpuId(infoEntity.getId());
        R save = couponFeignServive.save(spuBoundTo);
        if (save.getCode() != 0) {
            log.info("保存保存积分信息失败");
        }
        // 保存sku基本信息  pms_sku_info
        List<Skus> skus = spuInfo.getSkus();
        skus.stream().forEach(itm -> {
            List<Images> images = itm.getImages();
            // 获取默认图片
            String def = "";
            for (int i = 0; i < images.size(); i++) {
                if (images.get(i).getDefaultImg() == 1) {
                    def = images.get(i).getImgUrl();
                }
            }
            // 保存基本信息
            SkuInfoEntity skuInfo = new SkuInfoEntity();
            BeanUtils.copyProperties(itm, skuInfo);
            skuInfo.setBrandId(infoEntity.getBrandId());
            skuInfo.setCatalogId(infoEntity.getCatalogId());
            skuInfo.setSaleCount(0L);
            skuInfo.setSkuDefaultImg(def);
            skuInfo.setPrice(itm.getPrice());
            skuInfo.setSpuId(infoEntity.getId());
            skuInfoService.save(skuInfo);
            // 保存图片
            List<SkuImagesEntity> imagesEntities = images.stream().map(op -> {
                SkuImagesEntity imagesEntity = new SkuImagesEntity();
                imagesEntity.setDefaultImg(op.getDefaultImg());
                imagesEntity.setImgUrl(op.getImgUrl());
                imagesEntity.setSkuId(skuInfo.getSkuId());
                return imagesEntity;
            }).filter(entity -> {
                return !StringUtils.isEmpty(entity.getImgUrl());
            }).collect(Collectors.toList());

            // 保存sku销售属性信息  pms_sku_sale_attr_value
            skuImagesService.saveBatch(imagesEntities);
            List<Attr> attr = itm.getAttr();
            List<SkuSaleAttrValueEntity> attrValueEntityList = attr.stream().map(attrValue -> {
                SkuSaleAttrValueEntity saleAttrValue = new SkuSaleAttrValueEntity();
                saleAttrValue.setAttrId(attrValue.getAttrId());
                saleAttrValue.setAttrName(attrValue.getAttrName());
                saleAttrValue.setSkuId(skuInfo.getSkuId());
                saleAttrValue.setAttrValue(attrValue.getAttrValue());
                return saleAttrValue;
            }).collect(Collectors.toList());
            skuSaleAttrValueService.saveBatch(attrValueEntityList);

            // 保存sku的优惠信息
            SkuReductionTo skuReductionTo = new SkuReductionTo();
            skuReductionTo.setSkuId(skuInfo.getSkuId());
            BeanUtils.copyProperties(itm, skuReductionTo);

            if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                R r = couponFeignServive.saveSkuReduction(skuReductionTo);
                if (r.getCode() != 0) {
                    log.info("远程保存SKU信息失败");
                }

            }

        });
    }

    @Override
    public void saveInfoDecript(SpuInfoDescEntity decript) {
        spuInfoDescService.save(decript);

    }

    @Override
    public PageUtils queryPageList(Map<String, Object> params) {

        QueryWrapper<SpuInfoEntity> Wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");

        if (!StringUtils.isEmpty(key)) {
            Wrapper.and(itm -> {

                itm.eq("id", key).or().like("spu_name", key);
            });

        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {

            Wrapper.eq("publish_status", status);

        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            Wrapper.eq("brand_id", brandId);

        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            Wrapper.eq("catalog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                Wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void spuIdsaveEs(Long spuId) {

        List<SkuInfoEntity> infoEntities = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));

        // 查询当前SKU的所以可以被用来检索的规格属性
        List<ProductAttrValueEntity> productAttrValue = productAttrValueService.selectSpuId(spuId);
        // 收集id
        List<Long> longList = infoEntities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        List<Long> collect = productAttrValue.stream().map(itm -> {
            return itm.getAttrId();
        }).collect(Collectors.toList());
        List<Long> attrs = attrService.selectSearAttrs(collect);

        Set<Long> set = new HashSet<>(attrs);

        // 转换可检索数据
        List<SkuEsModel.Attrs> attrsis = productAttrValue.stream().filter(attr -> {
            return set.contains(attr.getAttrId());
        }).map(itmes -> {
            SkuEsModel.Attrs attrsi = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(itmes, attrsi);
            return attrsi;
        }).collect(Collectors.toList());


        Map<Long, Boolean> stockMap = null;

        try {
            // 远程查询是否有库存
            R skusHasStock = wareFeignService.getSkusHasStock(longList);
            // 把返回结果转成map
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {
            };
            stockMap = skusHasStock.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, itm -> itm.getHasStock()));

        } catch (Exception e) {
            log.info("库存查询异常：{}", e);

        }
        // 组装远程存入ES的实体类
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> skuEsModel = infoEntities.stream().map(itm -> {
            SkuEsModel skuEsModel1 = new SkuEsModel();
            BeanUtils.copyProperties(itm, skuEsModel1);
            // 默认图片
            skuEsModel1.setSkuImg(itm.getSkuDefaultImg());
            // 价格
            skuEsModel1.setSkuPrice(itm.getPrice());
            // 设置库存信息
            if (finalStockMap == null) {
                skuEsModel1.setHasStock(true);
            } else {
                skuEsModel1.setHasStock(finalStockMap.get(itm.getSkuId()));
            }

            // 刚上架的热度默认为0
            skuEsModel1.setHotScore(0L);
            // 查询品牌名字和分类信息
            BrandEntity byId = brandService.getById(skuEsModel1.getBrandId());
            skuEsModel1.setBrandImg(byId.getName());
            skuEsModel1.setBrandImg(byId.getLogo());
            // 设置分类名字
            CategoryEntity categoryEntity = categoryService.getById(skuEsModel1.getCatalogId());
            skuEsModel1.setCatalogName(categoryEntity.getName());
            skuEsModel1.setAttrs(attrsis);

            return skuEsModel1;
        }).collect(Collectors.toList());

        // 发送给ES 保存

        R startusUp = searchFeignService.productStartusUp(skuEsModel);
        if (startusUp.getCode() == 0) {
            SpuInfoEntity infoEntity = new SpuInfoEntity();
            infoEntity.setUpdateTime(new Date());
            infoEntity.setPublishStatus(1);
            baseMapper.update(infoEntity, new UpdateWrapper<SpuInfoEntity>().eq("id", spuId));
        } else {

        }


    }

    @Override
    public SpuInfoEntity getSpuInfoById(Long skuId) {

        SkuInfoEntity byId = skuInfoService.getById(skuId);

        Long skuId1 = byId.getSpuId();
        SpuInfoEntity infoEntity = getById(skuId1);

        return infoEntity;
    }
}