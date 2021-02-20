package com.guli.guliproduclt.vo;

import lombok.Data;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/1
 */
@Data
public class SkuItemSaleAttrVo {

    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;

}