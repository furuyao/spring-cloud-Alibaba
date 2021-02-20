package com.guli.guliproduclt.vo;

import lombok.Data;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/16
 */
@Data
public class AttrRespVo extends AttVo {
    // 所属分类名字
    private String catelogName;
    //所属分组名字
    private String groupName;
    //分类完整路径
    private Long[] catelogPath;



}
