package com.guli.gulike.aoth.controller;

import com.alibaba.fastjson.TypeReference;
import com.guli.common.constant.AuthServerConstant;
import com.guli.common.exception.BizCodeEnume;
import com.guli.common.utils.R;
import com.guli.common.vo.MemberRerspVo;
import com.guli.gulike.aoth.feign.MenberFeignService;
import com.guli.gulike.aoth.feign.SmsFeign;
import com.guli.gulike.aoth.vo.UserLoginVo;
import com.guli.gulike.aoth.vo.UserRegistVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/2
 */
@Controller
public class LoginController {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    SmsFeign smsFeign;

    @Autowired
    MenberFeignService menberFeignService;

    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phone) {
        // 防止盗刷


        // 60秒内不能在发
        String key = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone);
        if (!StringUtils.isEmpty(key)) {
            long l = Long.parseLong(key.split("_")[1]);
            if (System.currentTimeMillis() - l < 60000) {
                return R.error(BizCodeEnume.SMS_CODE_EXCETION.getCode(), BizCodeEnume.SMS_CODE_EXCETION.getMsg());

            }

        }

        String substring = UUID.randomUUID().toString().substring(0, 5) + "_" + System.currentTimeMillis();
        String[] split = substring.split("_");
        // 缓存验证码用于验证
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phone, substring, 10, TimeUnit.MINUTES);
        String code = "【云通知】您的验证码是" + split[0] + "。如非本人操作，请忽略本短信";
        smsFeign.sendCode(phone, code);
        return R.ok();
    }


    @PostMapping("/reg")
    public String regist(@Valid UserRegistVo vo, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {


            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors", errors);
            // js303 校验有问题就反回注册页
            return "redirect:/http:/auth.gullimall.com/reg.html";
        }


        String code = vo.getCode();

        String key = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());

        if (!StringUtils.isEmpty(key)) {
            String[] s = key.split("_");
            if (code.equals(s[0])) {

                // 删除验证码
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + vo.getPhone());

                // 真正的注册
                R regist = menberFeignService.regist(vo);
                if (regist.getCode() == 0) {

                    // 成功

                    return "redirect:http://auth.gulimall.com/login.html";
                } else {
                    Map<String, Object> errors = new HashMap<>();
                    errors.put("msg", regist.getData("msg", new TypeReference<String>() {
                    }));

                    redirectAttributes.addFlashAttribute("errors", errors);

                    return "redirect:http://auth.gulimall.com/reg.html";
                }


            } else {

                Map<String, String> errors = new HashMap<>();
                errors.put("code", "验证码错误");
                redirectAttributes.addFlashAttribute("errors", errors);
                return "redirect:http://auth.gulimall.com/reg.html";
            }

        } else {

            Map<String, String> errors = new HashMap<>();
            errors.put("code", "验证码错误");
            redirectAttributes.addFlashAttribute("errors", errors);
            return "redirect:http://auth.gulimall.com/reg.html";
        }


    }


    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes, HttpSession session) {
        R login = menberFeignService.login(vo);
        if (login.getCode() == 0) {
            MemberRerspVo msg = login.getData("msg", new TypeReference<MemberRerspVo>() {
            });
            session.setAttribute(AuthServerConstant.LOGIN_USER,msg);

            return "redirect:http://gulimall.com";
        } else {
            Map<String, String> stringMap = new HashMap<>();
            stringMap.put("msg", login.getData("msg", new TypeReference<String>() {
            }));


            redirectAttributes.addFlashAttribute("errors", stringMap);
            return "redirect:http://auth.gulimall.com/login.html";
        }

    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session){

        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);

        if (attribute == null){

            return "login";

        }else {

            return "redirect:http://gulimall.com";
        }


    }




}
