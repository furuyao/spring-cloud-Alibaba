package com.guli.giliorder.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AlipayTemplate {

    //在支付宝创建的应用的id
    private   String app_id = "2021000116658634" ;

    // 商户私钥，您的PKCS8格式RSA2私钥
    private  String merchant_private_key ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC1Nst4LkmDwpP1FmCySAGN2/0aCbJlUY68VCtKWL7L3b3FMqmcHIGS632HXoUupgApJS+r5q6V9qbtyDrvHWndQSel2s+5AuQk78CFSHvQzEUsmMvttCzQUqpVhyRQDTbe/D1aS+T1YzfRkf29pRa7m7vH5ZivUQxzPi0tup8d1QcDORFnTvXDemtl+8LvwLZ/p3asYZYop4jIHjlL8p+zEWX3mliN7hzDFXWuFc/5G4ts4ZEjKzcCbxwGYA7hZzhS1MBwe8tuWKjO8R0reS4aFJsoqh5ZFrx+UBLfrIF/XDu9jGb0O4t5aNsi+JOD9E127F/GZtvrrznvzDb0CsLXAgMBAAECggEBAKIUbW6Dr/sqeN24cypiXwl73uiADdCzXsAxZDruH1EGdqq/Qpa/pGE9QZxnPSXZEcG7YeUVwcedwfZhpdyi6UIc+ZJNoIcw/8/NpNtRVnUNjz1xOMzV31NA7NrssdzrxM0yvMlbomCcqptGQbZyx19rzkvfnm4IRuN4lD4XciPiF37+hL6qRzVPVLuZWv03g4oWm9Mom1xtEbNOARsY7m/c3e4mnvrHRsVF30m7SqU8irRb3pdA11eFgbI+x8CD3bJS0/MjR7tNVnwbIv9nocqMvsVSc5/K0YnlwRrtlm6eccUgQ2XYaY0N3Ngqi9qR5HW7F+nsnrbLfgX/sZls9MECgYEA7Rbu5LMro/M5uZnVzlJvDXx6DG5v21h7KQnkfPbwBxTysEPKV2rQ4/anpKVksIfUYzl+KrDXzzNG69euxC9m8lZ4ItLVvYtAVZsFD3V17nAyyOpvkBvlTJCRz0zFhdJxqZsTY0iZlfJtYwCPXzQwjRcFBz4CbwNXv6q2zw6W0iECgYEAw6r0UX9l37o4tfowNRvbHUblia3We+opVXKX5MKRpDfbhgvEqXK2SSkZ2iMf9JMmciqUy4GTG/StasnqC5hEGxtCJOKpBkxpuE3+CsqO6SnUXTYawd5b4KV9IArnpBiuSE+l9M7Ub1xZ1+ss8roREPU7x/KgFtI9+kCfFnm/ZfcCgYEAyy5wJIZ3y9VRjwGK/XofucsHPUgXjD7TrWPQ8FcfdLI7GVu/OBNOWU60dKFZHQAKv84xlZxgFi23hb4dA/wrmVsJMa7Fhd6kj7h+KbVcD68PlqdyXGnLpEXw71q3m+uxhMxlXRyGor5TiBiexdQQF5m2sXIu27xdouBGMkQqdIECgYBX8nIoY3vVyK0O1mpupJujGCe574H1ZnW3q28samvuBRFNueJmS2I1lhzBmIIXe0CPliYVJqOJsDt2QVVKauFJ0cG0NJDGfPgT2DJ0hUEg+iUy1EvIkfp4Ccoo6d3eOx2y8OHmHEUN3NOhIlqsqwdauO9q2tosB8nFvuACySxHVwKBgF9RgNZ3kZaNaV21D2+s2KKTapQFJWp4NeGbQjpO/7qOAh+JiDywmXFU5uLqSB2lNeIUUwiQpp2mMSmhNr77DDF3nAkfFR2E6WekG5epRv7kxgefCxV+AN9TiVGiHuik0v4ynYuXSVqFmun3EDRwb5CPW9DlQ87ZAWb8O2sADBxV" ;
    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    private  String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp1Rfz4ZrRERdKnwc7h8Cwy2E6GHKUod02NqPj39jowUYFYTAbdSb+P0bhPosM821Tt9qwyvtJQl+USvHqJAQM5gzHY1PlbA1JBuoLtjqqBbYhmeyeHGRR2f3c662To2hDZpHpm6Td21z3KYwsv3nrnJ3p7rGh/xRT8qkC8FOQ3RyxHuCbvovb+17PHpomuzxojjXiBs5iboVlWHfd6Xz/ARX/lP6PbPsCCShVcKvSsNCScgwoFBi2131x6f1ojoikdnCnXr9Kzn5omnUiOYnQUnfMW++TNJMJnPYZv83Vx+7cCv1vdneAhHv3+iU4q/c69lnYvo7EsfdRWbfjBsnYQIDAQAB" ;
    // 服务器[异步通知]页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    // 支付宝会悄悄的给我们发送一个请求，告诉我们支付成功的信息
    private  String notify_url = "http://owzfxgescw.52http.net/paymentNotifications";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    //同步通知，支付成功，一般跳转到成功页
    private  String return_url = "http://member.gulimall.com/memberOrder.html";

    // 签名方式
    private  String sign_type = "RSA2";

    // 字符编码格式
    private  String charset = "utf-8" ;

    private String timeout = "30m";

    // 支付宝网关； https://openapi.alipaydev.com/gateway.do
    private  String gatewayUrl ="https://openapi.alipaydev.com/gateway.do" ;

    public  String pay(PayVo vo) throws AlipayApiException {

        //AlipayClient alipayClient = new DefaultAlipayClient(AlipayTemplate.gatewayUrl, AlipayTemplate.app_id, AlipayTemplate.merchant_private_key, "json", AlipayTemplate.charset, AlipayTemplate.alipay_public_key, AlipayTemplate.sign_type);
        //1、根据支付宝的配置生成一个支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl,
                app_id, merchant_private_key, "json",
                charset, alipay_public_key, sign_type);

        //2、创建一个支付请求 //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(return_url);
        alipayRequest.setNotifyUrl(notify_url);

        //商户订单号，商户网站订单系统中唯一订单号，必填
        String out_trade_no = vo.getOut_trade_no();
        //付款金额，必填
        String total_amount = vo.getTotal_amount();
        //订单名称，必填
        String subject = vo.getSubject();
        //商品描述，可空
        String body = vo.getBody();

        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"timeout_express\":\""+timeout+"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        String result = alipayClient.pageExecute(alipayRequest).getBody();

        //会收到支付宝的响应，响应的是一个页面，只要浏览器显示这个页面，就会自动来到支付宝的收银台页面
        System.out.println("支付宝的响应："+result);

        return result;

    }
}
