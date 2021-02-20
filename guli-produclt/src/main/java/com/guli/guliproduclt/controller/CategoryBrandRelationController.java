package com.guli.guliproduclt.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.guli.guliproduclt.entity.BrandEntity;
import com.guli.guliproduclt.service.BrandService;
import com.guli.guliproduclt.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.guliproduclt.entity.CategoryBrandRelationEntity;
import com.guli.guliproduclt.service.CategoryBrandRelationService;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;


/**
 * 品牌分类关联
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
@RestController
@RequestMapping("/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;


    /**
     * 查询品牌名
     */
    @GetMapping("/brands/list")
    public R getBrandsName(@RequestParam(value = "catId", required = true) Long catId) {

        List<BrandEntity> brandsList = categoryBrandRelationService.getBrandsName(catId);

        List<BrandVo> collect = brandsList.stream().map(itm -> {
            BrandVo categoryBrandRelation = new BrandVo();
            categoryBrandRelation.setBrandId(itm.getBrandId());
            categoryBrandRelation.setBrandName(itm.getName());
            return categoryBrandRelation;
        }).collect(Collectors.toList());

        return R.ok().put("data", collect);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //  @RequiresPermissions("guliproduclt:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //  @RequiresPermissions("guliproduclt:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("guliproduclt:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.saveClassification(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("guliproduclt:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //  @RequiresPermissions("guliproduclt:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @GetMapping("/catelog/list")
    public R selectBycatelog(long brandId) {
        List<CategoryBrandRelationEntity> brandList = categoryBrandRelationService.list(
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));

        return R.ok().put("data", brandList);
    }
}
