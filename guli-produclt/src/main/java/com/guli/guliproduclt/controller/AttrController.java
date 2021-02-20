package com.guli.guliproduclt.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.guli.guliproduclt.entity.AttrEntity;
import com.guli.guliproduclt.entity.ProductAttrValueEntity;
import com.guli.guliproduclt.service.AttrAttrgroupRelationService;
import com.guli.guliproduclt.service.AttrService;
import com.guli.guliproduclt.service.ProductAttrValueService;
import com.guli.guliproduclt.vo.AttVo;
import com.guli.guliproduclt.vo.AttrRespVo;
import com.guli.guliproduclt.vo.AttrgroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;


/**
 * 商品属性
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-15 22:29:44
 */
@RestController
@RequestMapping("/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;


    @Autowired
    ProductAttrValueService productAttrValueService;

    /**
     * @Author: fry
     * @Description: /product/attr/base/listforspu/{spuId}
     * @Param:
     * @Date: 2020/8/21 14:11
     */
    @RequestMapping("/base/listforspu/{spuId}")
    //  @RequiresPermissions("guliware:attr:list")
    public R listforspu(@PathVariable Long spuId) {
        List<ProductAttrValueEntity> attrValueEntities = productAttrValueService.selectSpuId(spuId);
        return R.ok().put("data", attrValueEntities);
    }


    /**
     * @Author: fry
     * @Description: 分页查询加模糊查询
     * @Param:
     * @Date: 2020/8/16 10:29
     */
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,
                          @PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType") String type) {

        PageUtils page = attrService.queryBsesPage(params, catelogId, type);

        return R.ok().put("data", page);
    }


    /**
     * 列表    /product/spuinfo/save
     */
    @RequestMapping("/list")
    //  @RequiresPermissions("guliware:attr:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息   /product/spuinfo/save
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId) {
//        AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attr = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("guliware:attr:save")
    public R save(@RequestBody AttVo attr) {
        attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("guliware:attr:update")
    public R update(@RequestBody AttrRespVo attr) {
        attrService.updateAttr(attr);

        return R.ok();
    }


    /**
     * 修改  /product/attr/update/{spuId}
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuId(@RequestBody List<ProductAttrValueEntity> attr,
                         @PathVariable("spuId") Long spuId) {
        productAttrValueService.updateSpuId(attr, spuId);
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    //  @RequiresPermissions("guliware:attr:delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
