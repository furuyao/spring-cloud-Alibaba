package com.guli.giliorder.web;

import com.alipay.api.AlipayApiException;
import com.guli.giliorder.config.AlipayTemplate;
import com.guli.giliorder.config.PayVo;
import com.guli.giliorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/12
 */
@Controller
public class PayWebController {

    @Autowired
    AlipayTemplate alipayTemplate;
    @Autowired
    OrderService orderService;

    @ResponseBody
    @GetMapping(value = "/payOrder",produces = "text/html")
    public String payOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {


        PayVo payVo = orderService.getOrderPay(orderSn);


        String pay = alipayTemplate.pay(payVo);

        return pay;
    }

}
