package com.guli.guliware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.guli.common.exception.BizCodeEnume;
import com.guli.common.exception.NoStockException;
import com.guli.common.to.SkuHasStockVo;
import com.guli.guliware.vo.WareSkuLokVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.guliware.entity.WareSkuEntity;
import com.guli.guliware.service.WareSkuService;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;


/**
 * 商品库存
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:33:31
 */
@RestController
@RequestMapping("/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;


    /**
     * 锁定库存
     */
    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLokVo wareSkuLokVo) {

        try {
            Boolean lockStock = wareSkuService.orderLockStock(wareSkuLokVo);
            return R.ok();

        } catch (NoStockException e) {
            return R.error(BizCodeEnume.NO_STOC_EXCETION.getCode(), BizCodeEnume.NO_STOC_EXCETION.getMsg());

        }
    }


    /**
     * 查询是否有库存
     */
    @PostMapping("/hasstock")
    public R getSkusHasStock(@RequestBody List<Long> skuId) {

        List<SkuHasStockVo> skuEsModels = wareSkuService.getSkusHasStock(skuId);

        return R.ok().setData(skuEsModels);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    //  @RequiresPermissions("guliware:waresku:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //  @RequiresPermissions("guliware:waresku:info")
    public R info(@PathVariable("id") Long id) {
        WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("guliware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("guliware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku) {
        wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //  @RequiresPermissions("guliware:waresku:delete")
    public R delete(@RequestBody Long[] ids) {
        wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
