package com.guli.guliware.vo;

import lombok.Data;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/20
 */
@Data
public class MergeVo {

    /**
     *  purchaseId: 1, //整单id
     *   items:[1,2,3,4] //合并项集合
     */
    private Long purchaseId;

    private List<Long> items;

}
