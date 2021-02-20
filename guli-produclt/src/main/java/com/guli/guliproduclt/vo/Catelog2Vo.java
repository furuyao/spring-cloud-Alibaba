package com.guli.guliproduclt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @创建人: fry
 * @用于： 二级分类
 * @创建时间 2020/8/25
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catelog2Vo {
    // 1级分类id
    private String catalog1Id;
    // 三级分类
    private List<Catelog3Vo> catalog3List;

    private  String name;

    private String id;

    /**
    *@Author: fry
    *@Description: 三级分类
    *@Param:
    *@Date: 2020/8/25 18:01
    */


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catelog3Vo{
        private String catalog2Id;

        private String id;

        private String name;


    }

}
