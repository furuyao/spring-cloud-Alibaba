package com.guli.guliware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.guli.guliware.vo.MergeVo;
import com.guli.guliware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.guliware.entity.PurchaseEntity;
import com.guli.guliware.service.PurchaseService;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;



/**
 * 采购信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:33:31
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;


    /**
     * 完成采购/ware/purchase/done
     */
    @PostMapping("/done")
    //  @RequiresPermissions("guliware:purchase:list")
    public R done(@RequestBody PurchaseDoneVo doneVo){
        purchaseService.done(doneVo);
        return R.ok();
    }



    /**
     * 领取采购单 /ware/purchase/received
     */
    @PostMapping("/received")
    //  @RequiresPermissions("guliware:purchase:list")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);

        return R.ok();
    }


    /**
     * 合并采购单
     */
    @PostMapping("/merge")
    //  @RequiresPermissions("guliware:purchase:list")
    public R merge(@RequestBody MergeVo params){
        purchaseService.queryPageMerge(params);

        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
  //  @RequiresPermissions("guliware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 根据条件搜索
     */
    @RequestMapping("/unreceive/list")
    //  @RequiresPermissions("guliware:purchase:list")
    public R listUnreceive(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPagePurchase(params);

        return R.ok().put("page", page);
    }




    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
  //  @RequiresPermissions("guliware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("guliware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("guliware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("guliware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
