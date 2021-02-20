package com.guli.guliproduclt.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.guliproduclt.dao.ProductAttrValueDao;
import com.guli.guliproduclt.entity.ProductAttrValueEntity;
import com.guli.guliproduclt.service.ProductAttrValueService;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );


//        Page<ProductAttrValueEntity> pae = new Page<ProductAttrValueEntity>(123L, 1L);
//
//              String  start_date = "";
//              String  end_date ="";
//        IPage<ProductAttrValueEntity> result = this.page(page,
//                new LambdaQueryWrapper<ProductAttrValueEntity>()
//                        .apply(StrUtil.isNotBlank(start_date),
//                                "date_format (optime,'%Y-%m-%d') >= date_format('" + start_date + "','%Y-%m-%d')")
//                        .apply(StrUtil.isNotBlank(end_date),
//                                "date_format (optime,'%Y-%m-%d') <= date_format('" + end_date + "','%Y-%m-%d')")
//                        .orderByDesc(ProductAttrValueEntity::getAttrId));




        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> selectSpuId(Long spuId) {

        List<ProductAttrValueEntity> entities = this.baseMapper.selectList(
                new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        return entities;
    }

    @Override
    public void updateSpuId(List<ProductAttrValueEntity> attr, Long spuId) {

        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));

        List<ProductAttrValueEntity> collect = attr.stream().map(itm -> {
            itm.setSpuId(spuId);
            return itm;
        }).collect(Collectors.toList());

        this.saveBatch(collect);

    }

}