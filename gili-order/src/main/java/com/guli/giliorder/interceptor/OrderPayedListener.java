package com.guli.giliorder.interceptor;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.guli.giliorder.config.AlipayTemplate;
import com.guli.giliorder.config.PayAsyncVo;
import com.guli.giliorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/12
 */
@RestController
public class OrderPayedListener {

    @Autowired
    OrderService orderService;


    @Resource
    private AlipayTemplate alipayTemplate;

    /**
     * @Author: fry
     * @Description: 支付包异步回调信息
     * @Param: [vo, request]
     * @Date: 2020/9/12 15:14
     */
    @PostMapping("/paymentNotifications")
    public String handleAlipayed(PayAsyncVo vo, HttpServletRequest request) throws AlipayApiException, UnsupportedEncodingException {

        // 只要收到支付宝的异步通知，返回 success 支付宝便不再通知
        // 获取支付宝POST过来反馈信息
        // 验证签名
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//                 valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(),
                alipayTemplate.getCharset(), alipayTemplate.getSign_type()); //调用SDK验证签名

        String key = "";

        if (signVerified) {
            key = orderService.handlePayResult(vo);

        } else {

            key = "error";
        }

        // 只要收到通过就返回  success 这样支付宝才不会一值推送支付成功的信息
        return key;
    }


}
