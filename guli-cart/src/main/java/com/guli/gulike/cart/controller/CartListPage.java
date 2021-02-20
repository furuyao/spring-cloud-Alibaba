package com.guli.gulike.cart.controller;

import com.guli.gulike.cart.service.CartService;
import com.guli.gulike.cart.vo.Cart;
import com.guli.gulike.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/6
 */
@Controller
public class CartListPage {

    @Autowired
    CartService cartService;

    /**
    *@Author: fry
    *@Description: 获取被选中的物品
    *@Param: []
    *@Date: 2020/9/8 16:14
    */
    @ResponseBody
    @GetMapping("/currentUserCartItems")
    public List<CartItem> getCurrentUserCartItems(){

        return cartService.getCurrentUserCartItems();
    }



    @PostMapping("/clearCartProduct")
    public String clearCartProduct(@RequestParam("skuIds") List<String> skuIds) {

        cartService.deleteItem(skuIds);

        return "redirect:http://cart.gulimall.com/cart.html";
    }


    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("count") Integer count) {

        cartService.countItem(skuId, count);

        return "redirect:http://cart.gulimall.com/cart.html";

    }


    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("check") Integer check) {

        cartService.chectItem(skuId, check);

        return "redirect:http://cart.gulimall.com/cart.html";

    }

    @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {

        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }


    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                            RedirectAttributes ra) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId, num);

        ra.addAttribute("skuId", skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";

    }

    /**
     * 为了防止刷新就添加已次问题所以改成重定向刷新
     *
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId, Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();


        // 再次查询购物车数据
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item", cartItem);
        return "success";

    }

}
