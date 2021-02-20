package com.guli.guliproduclt.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import com.guli.guliproduclt.entity.SkuInfoEntity;
import com.guli.guliproduclt.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;


/**
 * sku信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-19 10:39:18
 */
@RestController
@RequestMapping("/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;



    /**
     * 获取价格
     */
    @GetMapping("/{skuId}/price")
    public R getPrice(@PathVariable("skuId") Long skuId) {

        BigDecimal price = skuInfoService.getById(skuId).getPrice();

        return R.ok().setData(price.toString());
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //  @RequiresPermissions("guliware:skuinfo:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuInfoService.queryPageList(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId) {
        SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("guliware:skuinfo:save")
    public R save(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("guliware:skuinfo:update")
    public R update(@RequestBody SkuInfoEntity skuInfo) {
        skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //  @RequiresPermissions("guliware:skuinfo:delete")
    public R delete(@RequestBody Long[] skuIds) {
        skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}
