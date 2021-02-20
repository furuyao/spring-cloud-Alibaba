package com.guli.guliware.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

import com.guli.guliware.vo.FareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.guliware.entity.WareInfoEntity;
import com.guli.guliware.service.WareInfoService;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;



/**
 * 仓库信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:33:31
 */
@RestController
@RequestMapping("/wareinfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;



    @GetMapping("/fare")
    public R getFare(@RequestParam("addrId") Long addrId){

        FareVo decimal = wareInfoService.getFare(addrId);

        return R.ok().setData(decimal);
    }




    /**
     * 列表
     */
    @RequestMapping("/list")
  //  @RequiresPermissions("guliware:wareinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
  //  @RequiresPermissions("guliware:wareinfo:info")
    public R info(@PathVariable("id") Long id){
		WareInfoEntity wareInfo = wareInfoService.getById(id);

        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("guliware:wareinfo:save")
    public R save(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.save(wareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("guliware:wareinfo:update")
    public R update(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.updateById(wareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("guliware:wareinfo:delete")
    public R delete(@RequestBody Long[] ids){
		wareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
