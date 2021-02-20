package com.guli.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
*@Author: fry
*@Description: 分页工具
*@Param: object 分页对象 currentPage 当前页  pageSize 每页数量
*@Date: 2020/10/24 17:21
*/
public  class PageHelper {
    public static Page page(Object object,int currentPage,int pageSize) {
        Page page = new Page();
        List<Object> list = (List<Object>) object;
        List<Object> pageObjects = new ArrayList<>();
        if (list != null || list.size() > 0) {
            int index = currentPage > 1 ? (currentPage - 1) * pageSize : 0;
            for (int i = 0; i < pageSize && i < list.size() - index; i++) {
                Object o = list.get(i + index);
                pageObjects.add(o);
            }
        }
        page.setCurrentPage(currentPage);
        page.setObject(pageObjects);
        page.setPageSize(pageSize);
        page.setTotalCount(list.size());
        return page;
    }
    public static void main(String[] args) {
        List<String> list=new ArrayList<>();
        for (int i=0;i<50;i++){
            list.add(String.valueOf(i));
        }
        Page page = page(list, 2, 10);
        System.out.println(page(list,2,10));
    }
}
