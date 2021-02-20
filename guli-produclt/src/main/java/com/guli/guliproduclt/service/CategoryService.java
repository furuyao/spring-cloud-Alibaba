package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.CategoryEntity;
import com.guli.guliproduclt.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 作者 FRY
     * 功能描述 : 三级分类查询分类
     * 日期 2020/6/15 19:22
     * 参数
     * 返回值 java.util.List<com.guli.guliproduclt.entity.CategoryEntity>
     */
    List<CategoryEntity> listWhiBel();

   /**
   *@Description: 踩踩踩
   *@Param: [asList]
   *@return: void
   *@Author: fry
   *@Date: 2020/8/15 13:03
   *@Version: 1.0
   *@Company: 版权所有
   */
    void rremoveMenuById(List<Long> asList);

    /**
    *@Author: fry
    *@Description: 查询父节点
    *@Param: [catelogId]
    *@Date: 2020/8/15 13:05
    */
    Long[] findCcatelogPath(Long catelogId);

    /**
    *@Author: fry
    *@Description: 跟新冗余
    *@Param: [category]
    *@Date: 2020/8/15 17:59
    */
    void updateClassification(CategoryEntity category);

    /**
    *@Author: fry
    *@Description: 首页父类菜单列表查询
    *@Param: []
    *@Date: 2020/8/25 17:13
    */
    List<CategoryEntity> selectCategorys();

    /**
    *@Author: fry
    *@Description: 查询父分类下的所有
    *@Param: []
    *@Date: 2020/8/25 18:38
    */
    Map<String, List<Catelog2Vo>> getCatalogJson();
}

