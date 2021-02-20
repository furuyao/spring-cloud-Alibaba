package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.BrandEntity;
import com.guli.guliproduclt.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
    *@Author: fry
    *@Description: 新增关联查询
    *@Param: [categoryBrandRelation]
    *@Date: 2020/8/15 17:20
    */

    void saveClassification(CategoryBrandRelationEntity categoryBrandRelation);
    /**
    *@Author: fry
    *@Description: 跟新品牌冗余字段
    *@Param: [name, brandId]
    *@Date: 2020/8/15 17:56
    */

    void updateCard(String name, Long brandId);

    /**
    *@Author: fry
    *@Description: 跟新分类冗余字段
    *@Param: [name, catId]
    *@Date: 2020/8/15 18:05
    */
    void updateClassification(String name, Long catId);
    /**
    *@Author: fry
    *@Description: 查询品牌名
    *@Param: [catId]
    *@Date: 2020/8/17 18:37
    */
    List<BrandEntity> getBrandsName(Long catId);
}

