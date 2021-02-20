package com.guli.gulicoupon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.guli.gulicoupon.entity.SeckillSessionEntity;
import com.guli.gulicoupon.service.SeckillSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;



/**
 * 秒杀活动场次
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-19 14:50:17
 */
@RestController
@RequestMapping("/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;


    /**
     * 扫描三天内要秒杀的活动
     */
    @GetMapping("/lates3DaySession")
    public R getKilSkuLatest3Days(){

       List<SeckillSessionEntity> sessionEntities = seckillSessionService.getKilSkuLatest3Days();
        return R.ok().setData(sessionEntities);
    }



    /**
     * 列表
     */
    @RequestMapping("/list")
  //  @RequiresPermissions("guliware:seckillsession:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
  //  @RequiresPermissions("guliware:seckillsession:info")
    public R info(@PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
   // @RequiresPermissions("guliware:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.save(seckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
   // @RequiresPermissions("guliware:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("guliware:seckillsession:delete")
    public R delete(@RequestBody Long[] ids){
		seckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
