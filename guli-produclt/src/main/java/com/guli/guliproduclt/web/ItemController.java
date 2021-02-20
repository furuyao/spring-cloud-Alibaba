package com.guli.guliproduclt.web;

import com.guli.guliproduclt.service.SkuInfoService;
import com.guli.guliproduclt.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/1
 */
@Controller
public class ItemController {
    @Autowired
    SkuInfoService skuInfoService;

    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model){

       SkuItemVo skuItem = skuInfoService.getSkuItem(skuId);

            model.addAttribute("item",skuItem);
        return "item";
    }

}
