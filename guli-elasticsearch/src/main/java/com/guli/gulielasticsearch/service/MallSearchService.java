package com.guli.gulielasticsearch.service;

import com.guli.gulielasticsearch.vo.SearchParam;
import com.guli.gulielasticsearch.vo.SearchResult;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/28
 */
public interface MallSearchService {
    /**
    *@Author: fry
    *@Description: 用于页面检索
    *@Param: [searchParam]
    *@Date: 2020/8/28 15:31
    */
    SearchResult selectSearch(SearchParam searchParam);
}
