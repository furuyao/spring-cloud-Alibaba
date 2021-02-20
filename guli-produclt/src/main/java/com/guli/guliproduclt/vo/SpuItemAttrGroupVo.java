package com.guli.guliproduclt.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/1
 */
@Data
@ToString
public  class SpuItemAttrGroupVo {

    private String groupName;

    private List<Attr> attrs;


}

