package com.guli.gulike.secki.controller;

import com.guli.common.to.SeckillSkuRelationTo;
import com.guli.common.utils.R;
import com.guli.gulike.secki.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
@Controller
public class SeckillController {


    @Autowired
    SeckillService seckillService;

    @GetMapping("/currentSeckillSkus")
    @ResponseBody
    public R getCurrentSeckillSkus() {

        List<SeckillSkuRelationTo> sessionTos = seckillService.getCurrentSeckillSkus();
        return R.ok().setData(sessionTos);
    }
    @ResponseBody
    @GetMapping("/sku/seckill/{skuId}")
    public R skuSeckiIofo(@PathVariable("skuId") Long skuId) {


        SeckillSkuRelationTo to=seckillService.getSkuSeckillInfo(skuId);

        return R.ok().setData(to);
    }


    @GetMapping("/kill")
    public String secKill(@RequestParam("killId") String killId,
                     @RequestParam("key") String key,
                     @RequestParam("num") Integer num,
                     Model model){

     String orderSn=  seckillService.secKill(killId,key,num);
        model.addAttribute("orderSn", orderSn);
    return "success";
    }



}
