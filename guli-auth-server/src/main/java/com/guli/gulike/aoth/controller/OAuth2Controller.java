package com.guli.gulike.aoth.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.guli.common.constant.AuthServerConstant;
import com.guli.common.utils.HttpUtils;
import com.guli.common.utils.R;
import com.guli.common.vo.MemberRerspVo;
import com.guli.gulike.aoth.feign.MenberFeignService;
import com.guli.gulike.aoth.vo.SocialUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/4
 */
@Controller
public class OAuth2Controller {

    @Autowired
    MenberFeignService menberFeignService;


    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session) throws Exception {
        // https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&redirect_uri=YOUR_REGISTERED_REDIRECT_URI&code=CODE

        Map<String,String> map = new HashMap<>();
        map.put("client_id","2174846235");
        map.put("client_secret","45c28e0e51f8bcc8064e599c2956ed1e");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/weibo/success");
        map.put("code",code);

        String post = HttpUtils.post("https://api.weibo.com/oauth2/access_token", null, map);


        if (post !=null){

            SocialUserVo socialUserVo = JSON.parseObject(post, SocialUserVo.class);

            R oauth = menberFeignService.oauth(socialUserVo);
            if (oauth.getCode()==0){
                MemberRerspVo data = oauth.getData("data", new TypeReference<MemberRerspVo>() {
                });
                session.setAttribute(AuthServerConstant.LOGIN_USER,data);
                return "redirect:http://gulimall.com";

            }else {
                return "redirect:http://auth.gulimall.com/login.html";

            }

        }else {

            return "redirect:http://auth.gulimall.com/login.html";

        }

    }

}
