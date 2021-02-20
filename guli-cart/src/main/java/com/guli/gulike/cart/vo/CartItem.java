package com.guli.gulike.cart.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/6
 */
//@Data
public class CartItem {

    // 商品id
    private Long skuId;

    // 选中状态
    private  Boolean check = true;

    // 时间
    private String title;

    // 商品图片

    private String image;
    // 商品
    List<String> skuAttr;

    //单价价
    private BigDecimal price;


    // 数量

    private Integer  count;

    // 当前商品总价
    private BigDecimal totalPrice;




    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long ckuId) {
        this.skuId = ckuId;
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

    /**
     * 计算总价
     * @return
     */
    public BigDecimal getTotalPrice() {
        if (getCheck()){

            return this.price.multiply(new BigDecimal(""+this.count));
        }else {

            return new  BigDecimal("0");
        }

    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


}
