package com.guli.guliproduclt.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.guli.guliproduclt.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.guliproduclt.entity.SpuInfoEntity;
import com.guli.guliproduclt.service.SpuInfoService;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;



/**
 * spu信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
@RestController
@RequestMapping("/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;


    /**
     * 根据SKU获取sku信息
     */
    @GetMapping("/skuId/{skuId}")
    public R getSpuInfoById(@PathVariable("skuId") Long skuId){

       SpuInfoEntity infoEntity = spuInfoService.getSpuInfoById(skuId);

        return R.ok().setData(infoEntity);
    }





    /**
     * 上架商品
     */
    @RequestMapping("/{spuId}/up")
    public R spuIdsaveEs(@PathVariable("spuId") Long spuId){
       spuInfoService.spuIdsaveEs(spuId);
        return R.ok();
    }



    /**
     * 列表
     */
    @RequestMapping("/list")
  //  @RequiresPermissions("guliproduclt:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageList(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
  //  @RequiresPermissions("guliproduclt:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("guliproduclt:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuInfo){
		spuInfoService.saveSpuinfo(spuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("guliproduclt:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("guliproduclt:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
