package com.guli.gulike.cart.service;

import com.guli.gulike.cart.vo.Cart;
import com.guli.gulike.cart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/6
 */
public interface CartService {

    /**
    *@Author: fry
    *@Description: 用于加入购物车和购物车中数量加减
    *@Param: [skuId, num]
    *@Date: 2020/9/7 10:06
    */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;
    /**
    *@Author: fry
    *@Description: 用于查询购物车中单个物品信息
    *@Param: [skuId]
    *@Date: 2020/9/7 10:06
    */
    CartItem getCartItem(Long skuId);

    /**
    *@Author: fry
    *@Description: 查询购物车并且合并临时购物车
    *@Param: []
    *@Date: 2020/9/7 10:04
    */

    Cart getCart() throws ExecutionException, InterruptedException;


    /**
    *@Author: fry
    *@Description: 清空购物车
    *@Param: [cartkey]
    *@Date: 2020/9/7 10:41
    */
    void clearCart(String cartkey);

    /**
    *@Author: fry
    *@Description: 勾选购物项
    *@Param: [skuId, check]
    *@Date: 2020/9/7 14:29
    */
    void chectItem(Long skuId, Integer check);

    /**
    *@Author: fry
    *@Description: 数量加减
    *@Param: [skuId, count]
    *@Date: 2020/9/7 15:08
    */

    void countItem(Long skuId, Integer count);

    /**
    *@Author: fry
    *@Description: 删除购物车
    *@Param: [skuIds]
    *@Date: 2020/9/7 15:24
    */
    void deleteItem(List<String> skuIds);

    /**
    *@Author: fry
    *@Description: 获取被选中的物品
    *@Param: []
    *@Date: 2020/9/8 16:15
    */
    List<CartItem> getCurrentUserCartItems();

}
