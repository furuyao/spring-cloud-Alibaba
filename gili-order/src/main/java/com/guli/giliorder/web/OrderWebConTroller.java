package com.guli.giliorder.web;

import com.guli.giliorder.service.OrderService;
import com.guli.giliorder.vo.OrderConfirmVo;
import com.guli.giliorder.vo.SubmitOderResponseVo;
import com.guli.giliorder.vo.SubmitOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/8
 */
@Controller
public class OrderWebConTroller {

    @Autowired
    OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {

        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData", confirmVo);
        return "confirm";
    }


    @PostMapping("/submitOrder")
    public String submitOrder(SubmitOrder order, Model model, RedirectAttributes redirectAttributes) {

        SubmitOderResponseVo vo = orderService.submitOrder(order);

        if (vo.getCoder() == 0) {

            //返回成功  ，返回支付选着页
            model.addAttribute("submitOrderResponse", vo);

            return "pay";
        } else {
            String msg = "下单失败";
            switch (vo.getCoder()) {
                case 1:
                    msg = "令牌校验失败，请刷新在提交";
                    break;
                case 2:
                    msg = "订单商品价格发生变化，请确认在提交";
                    break;
                case 3:
                    msg = "库存锁定失败，商品库存不足";
                    break;
            }

            redirectAttributes.addAttribute("msg", msg);
            return "redirect:http://order.gilimall.com/toTrade";
        }

    }

}
