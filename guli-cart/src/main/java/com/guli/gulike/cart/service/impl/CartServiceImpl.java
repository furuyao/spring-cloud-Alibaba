package com.guli.gulike.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.guli.common.to.SkuInfoVo;
import com.guli.common.utils.R;
import com.guli.gulike.cart.feign.ProductFeignService;
import com.guli.gulike.cart.interceptor.CarInterceptor;
import com.guli.gulike.cart.service.CartService;
import com.guli.gulike.cart.to.UserInfoTo;
import com.guli.gulike.cart.vo.Cart;
import com.guli.gulike.cart.vo.CartItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/6
 */


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;


    @Autowired
    ThreadPoolExecutor executor;


    private final String CART_PREFIX = "gulimall:cart:";

    /**
     * @Author: fry
     * @Description: 用于单个加入购物车和购物车中的数量加减
     * @Param: [skuId, num]
     * @Date: 2020/9/7 10:03
     */

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();

        String reg = (String) cartOps.get(skuId.toString());
        // 判断redis中是否有该商品
        if (StringUtils.isEmpty(reg)) {
            CartItem cartItem = new CartItem();
            // 异步查询商品信息
            CompletableFuture<Void> skuInfo1 = CompletableFuture.runAsync(() -> {
                R info = productFeignService.info(skuId);

                SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });

                // 商品信息添加到购物车
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(skuInfo.getSkuDefaultImg());
                cartItem.setTitle(skuInfo.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(skuInfo.getPrice());

            }, executor);


            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);

                cartItem.setSkuAttr(skuSaleAttrValues);


            }, executor);


            CompletableFuture.allOf(skuInfo1, voidCompletableFuture).get();

            String key = JSON.toJSONString(cartItem);

            cartOps.put(skuId.toString(), key);
            return cartItem;
        } else {
            // 如果Rdis中有这个商品就直接改数据
            CartItem cartItem = JSON.parseObject(reg, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);

            String key = JSON.toJSONString(cartItem);

            cartOps.put(skuId.toString(), key);
            return cartItem;
        }


    }

    @Override
    public CartItem getCartItem(Long skuId) {

        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String key = (String) cartOps.get(skuId.toString());

        CartItem cartItem = JSON.parseObject(key, CartItem.class);

        return cartItem;
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        // 判断是否登录
        UserInfoTo userInfoTo = CarInterceptor.local.get();
        Long userId = userInfoTo.getUserId();

        if (userId != null) {
            // 登录了的
            String key = String.valueOf(userInfoTo.getUserId());
            // 先查询临时购物车如果有先合并
            String userKey = userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(userKey);
            if (cartItems != null) {
                for (CartItem itm : cartItems) {
                    addToCart(itm.getSkuId(), itm.getCount());
                }
                // 清空临时购物车数据
                clearCart(CART_PREFIX + userKey);
            }
            // 在查登录后的购物车【包括临时购物车的】
            List<CartItem> cartItemsTo = getCartItems(key);
            cart.setItems(cartItemsTo);
            return cart;
        } else {
            // 没登录
            String userKey = userInfoTo.getUserKey();
            List<CartItem> cartItems = getCartItems(userKey);
            cart.setItems(cartItems);
        }

        return cart;


    }

    @Override
    public void clearCart(String cartkey) {
        redisTemplate.delete(cartkey);
    }

    @Override
    public void chectItem(Long skuId, Integer check) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);

        cartItem.setCheck(check == 1 ? true : false);
        String key = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(), key);

    }

    @Override
    public void countItem(Long skuId, Integer count) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(count);
        String key = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(), key);
    }

    @Override
    public void deleteItem(List<String> skuIds) {
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();


        skuIds.forEach(cartOps::delete);


    }

    @Override
    public List<CartItem> getCurrentUserCartItems() {

        UserInfoTo userInfoTo = CarInterceptor.local.get();
        Long userId = userInfoTo.getUserId();

        if (userId == null) {

            return null;
        } else {
            List<CartItem> cartItems = getCartItems(userId.toString());
            // 过滤非选中的项
            List<CartItem> collect = cartItems.stream().filter(item -> item.getCheck()).map(obj -> {
                // 获取最新的价格
                R price = productFeignService.getPrice(obj.getSkuId());

                String key = (String) price.get("data");

                obj.setPrice(new BigDecimal(key));
                return obj;
            }).collect(Collectors.toList());
            return collect;
        }

    }

    /**
     * @Author: fry
     * @Description: 获取要操作的购物车
     * @Param: []
     * @Date: 2020/9/6 19:01
     */


    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CarInterceptor.local.get();

        String cartKey = "";

        if (userInfoTo.getUserId() != null) {
            // 登录状态
            cartKey = CART_PREFIX + userInfoTo.getUserId();

        } else {
            // 没登录状态
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }

        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps(cartKey);


        return boundHashOperations;
    }

    /**
     * @Author: fry
     * @Description: 获取指定购物车
     * @Param: [key]
     * @Date: 2020/9/7 9:40
     */

    private List<CartItem> getCartItems(String key) {
        String cartKey = CART_PREFIX + key;
        BoundHashOperations<String, Object, Object> boundHashOperations = redisTemplate.boundHashOps(cartKey);
        List<Object> values = boundHashOperations.values();
        if (values != null) {
            List<CartItem> collect = values.stream().map((item) -> {
                String item1 = (String) item;
                CartItem cartItem = JSON.parseObject(item1, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());

            return collect;

        }

        return null;
    }


}
