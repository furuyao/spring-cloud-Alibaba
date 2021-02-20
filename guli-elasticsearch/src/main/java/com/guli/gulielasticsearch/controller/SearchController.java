package com.guli.gulielasticsearch.controller;

import com.guli.gulielasticsearch.service.MallSearchService;
import com.guli.gulielasticsearch.vo.SearchParam;
import com.guli.gulielasticsearch.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/27
 */
@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model){

        SearchResult searchResult = mallSearchService.selectSearch(searchParam);
        model.addAttribute("result",searchResult);
        return "index";
    }

}
