package com.guli.guliware.vo;

import lombok.Data;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/21
 */
@Data
public class PurchaseItemDoneVo {
    /**
     * {itemId:1,status:4,reason:""}
     */
    private Long itemId;

    private Integer status;

    private String reason;


}
