package com.guli.guliproduclt.service.impl;

import com.guli.guliproduclt.dao.SkuSaleAttrValueDao;
import com.guli.guliproduclt.entity.SkuSaleAttrValueEntity;
import com.guli.guliproduclt.service.SkuSaleAttrValueService;
import com.guli.guliproduclt.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrBySpuId(Long spuId) {

         List<SkuItemSaleAttrVo>  skuItemSaleAttrVos=  this.baseMapper.getSaleAttrBySpuId(spuId);
        return skuItemSaleAttrVos;
    }

    @Override
    public List<String> getSkuSaleAttrValues(Long skuId) {

        List<String> strings=this.baseMapper.getSkuSaleAttrValues(skuId);


        return strings;
    }

}