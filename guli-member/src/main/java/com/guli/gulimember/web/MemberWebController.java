package com.guli.gulimember.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.guli.common.utils.R;
import com.guli.gulimember.feign.OrderFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/12
 */
@Controller
public class MemberWebController {

    @Autowired
    OrderFeign orderFeign;

    @GetMapping("/memberOrder.html")
    public String memberOrderPage(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, Model model) {

        Map<String, Object> map = new HashMap<>();

            map.put("page",pageNum.toString());
        R r = orderFeign.listWithItem(map);
        System.out.println(JSON.toJSONString(r));
        model.addAttribute("orders",r);
        return "orderList";
    }


}
