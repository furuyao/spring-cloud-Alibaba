package com.guli.gulike.cart.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/6
 */
public class Cart {

    //商品
    List<CartItem> items;

    // 商品数量
    private Integer countNum;
    // 商品类型
    private Integer countType;
    // 商品总价
    private BigDecimal totalAmount;
    //减免价格
    private BigDecimal reduce = new BigDecimal("0.00");

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    /**
     * 计算总数量
     * @return
     */
    public Integer getCountNum() {
      int count = 0;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                count = item.getCount();

            }

        }

        return count;
    }

    /**
     * 商品总类数
     * @return
     */
    public Integer getCountType() {

        int count = 0;
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                count = item.getCount();

            }

        }

        return count;
    }

    /**
     * 商品总价
     * @return
     */


    public BigDecimal getTotalAmount() {

        BigDecimal bigDecimal = new BigDecimal("0");

        // 计算总价
        if (items != null && items.size() > 0) {
            for (CartItem item : items) {
                BigDecimal totalPrice = item.getTotalPrice();

                bigDecimal = bigDecimal.add(totalPrice);


            }
        }
        // 减掉优惠
        bigDecimal.subtract(getReduce());

        return bigDecimal;

    }


    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}
