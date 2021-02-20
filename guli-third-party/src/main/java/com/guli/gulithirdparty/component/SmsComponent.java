package com.guli.gulithirdparty.component;

import com.guli.common.utils.HttpUtils;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * 发送短信的类
 *
 * @author fry
 * <br>FileName: SmsComponent
 * <br>Date: 2020/08/01 14:59:38
 */
@Component
@Data
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
public class SmsComponent {
    /**
     * 短信服务，请求地址 支持http 和 https 及 WEBSOCKET
     */
    private String host;
    /**
     * 短信服务，后缀
     */
    private String path;
    /**
     * 短信服务，签名
     */
    private String sign;
    /**
     * 短信服务，模板内容
     */
    private String skin;
    /**
     * 短信服务，appcode
     */
    private String appcode;

    /**
     * 发送短信的方法
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     */
    public void sendSsCode(String phoneNumber, String code) {
        String method = "GET";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("content", code);
        querys.put("mobile", phoneNumber);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
