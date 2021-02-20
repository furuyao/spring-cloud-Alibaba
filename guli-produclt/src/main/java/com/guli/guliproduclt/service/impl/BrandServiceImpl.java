package com.guli.guliproduclt.service.impl;

import com.guli.guliproduclt.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.guliproduclt.dao.BrandDao;
import com.guli.guliproduclt.entity.BrandEntity;
import com.guli.guliproduclt.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
   private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void updateClassification(BrandEntity brand) {
        //保证冗余一致
        this.updateById(brand);
        if (!StringUtils.isEmpty(brand.getName())){
          categoryBrandRelationService.updateCard(brand.getName(),brand.getBrandId());
            // TODO 标记后面还有要跟新的冗余
        }

    }


}