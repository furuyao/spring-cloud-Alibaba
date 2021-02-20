package com.guli.common.exception;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/10
 */
public class NoStockException extends RuntimeException{

    private String skuId;

    public NoStockException(String skuId){

        super("没有库存了");

    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {


        this.skuId = skuId;
    }
}
