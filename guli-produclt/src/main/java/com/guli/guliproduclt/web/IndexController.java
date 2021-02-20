package com.guli.guliproduclt.web;

import com.guli.guliproduclt.entity.CategoryEntity;
import com.guli.guliproduclt.service.CategoryService;
import com.guli.guliproduclt.vo.Catelog2Vo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/25
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        List<CategoryEntity> category = categoryService.selectCategorys();

        model.addAttribute("categorys", category);
        return "index";
    }

    /**
     * 分类查询    index/catalog.json
     */
    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();

        return catalogJson;
    }




}
