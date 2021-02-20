package com.guli.gulithirdparty.controller;

import com.guli.common.utils.R;
import com.guli.gulithirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/3
 */
@RestController
@RequestMapping("/sms")
public class SmsSendController {


    @Autowired
    SmsComponent smsComponent;

    @GetMapping("/component")
    public R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code){

        smsComponent.sendSsCode(phone,code);

        return R.ok();
    }



}
