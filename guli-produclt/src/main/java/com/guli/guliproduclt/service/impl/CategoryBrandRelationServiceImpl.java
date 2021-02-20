package com.guli.guliproduclt.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.guli.guliproduclt.entity.BrandEntity;
import com.guli.guliproduclt.entity.CategoryEntity;
import com.guli.guliproduclt.service.BrandService;
import com.guli.guliproduclt.service.CategoryService;
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

import com.guli.guliproduclt.dao.CategoryBrandRelationDao;
import com.guli.guliproduclt.entity.CategoryBrandRelationEntity;
import com.guli.guliproduclt.service.CategoryBrandRelationService;
import sun.dc.pr.PRError;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveClassification(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        CategoryEntity byId = categoryService.getById(catelogId);
        BrandEntity byId1 = brandService.getById(brandId);
        categoryBrandRelation.setCatelogName(byId.getName());
        categoryBrandRelation.setBrandName(byId1.getName());
        baseMapper.insert(categoryBrandRelation);
    }

    @Override
    public void updateCard(String name, Long brandId) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        this.update(categoryBrandRelationEntity, new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));

    }

    @Override
    public void updateClassification(String name, Long catId) {

        this.baseMapper.updateClassification(name, catId);
    }

    @Override
    public List<BrandEntity> getBrandsName(Long catId) {

        List<CategoryBrandRelationEntity> catelog = this.baseMapper.selectList(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> collect = catelog.stream().map(itm -> {
            Long brandId = itm.getBrandId();

            BrandEntity byId = brandService.getById(brandId);

            return byId;
        }).collect(Collectors.toList());

        return collect;
    }


}