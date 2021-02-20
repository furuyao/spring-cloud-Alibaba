package com.guli.gulithirdparty;

import com.aliyun.oss.OSSClient;
import com.guli.gulithirdparty.component.SmsComponent;
import com.guli.common.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class GuliThirdPartyApplicationTests {

    @Autowired
    OSSClient ossClient;

    @Autowired
    SmsComponent smsComponent;


    @Test
    void contextLoads() throws FileNotFoundException {

        InputStream inputStream = new FileInputStream("D:\\img\\QQ图片20200705145256.png");
        ossClient.putObject("gulifry","1.png",inputStream);
            System.out.println("上传成功");
    }



    public static void main(String[] args) {
        String host = "https://zwp.market.alicloudapi.com";
        String path = "/sms/sendv2";
        String method = "GET";
        String appcode = "3cfe2f50b9a64498bd9117e07cf4b9f6";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("content", "【云通知】您的验证码是123456。如非本人操作，请忽略本短信");
        querys.put("mobile", "13101391007");


        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
