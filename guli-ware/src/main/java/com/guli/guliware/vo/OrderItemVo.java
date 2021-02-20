package com.guli.guliware.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/8
 */

public class OrderItemVo {

    // 商品id
    private Long skuId;

    // 选中状态
    private Boolean check;

    // 时间
    private String title;

    // 商品图片

    private String image;
    // 商品
    List<String> skuAttr;

    //单价价
    private BigDecimal price;


    // 数量

    private Integer count;

    // 当前商品总价
    private BigDecimal subtotalPrice= new BigDecimal("1000");

    // 有货无货
    private boolean hasStock;
    // 总量
    private BigDecimal weifht;

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getSkuAttr() {
        return skuAttr;
    }

    public void setSkuAttr(List<String> skuAttr) {
        this.skuAttr = skuAttr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getSubtotalPrice() {

        BigDecimal multiply = getPrice().multiply(new BigDecimal(getCount().toString()));

        return multiply;
    }

    public void setSubtotalPrice(BigDecimal subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public boolean getHasStock() {
        return hasStock;
    }

    public void setHasStock(boolean hasStock) {
        this.hasStock = hasStock;
    }

    public BigDecimal getWeifht() {
        return weifht;
    }

    public void setWeifht(BigDecimal weifht) {
        this.weifht = weifht;
    }
}
