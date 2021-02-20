package com.guli.common.utils;

import lombok.Data;
import lombok.ToString;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/10/24
 */
@Data
@ToString
public class Page {
    private int currentPage;//当前页
    private int pageSize;   //每页条数
    private Object object;  //分页内容
    private int totalCount;//总条数

    public Page() {
    }
    public Page(int currentPage, int pageSize, Object object, int totalCount) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.object = object;
        this.totalCount = totalCount;
    }

}
