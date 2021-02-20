package com.guli.gulimember.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.guli.common.exception.BizCodeEnume;
import com.guli.gulimember.vo.MemberRegistVo;
import com.guli.gulimember.vo.MenberLoginVo;
import com.guli.gulimember.vo.SocialUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.gulimember.entity.MemberEntity;
import com.guli.gulimember.service.MemberService;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;



/**
 * 会员
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 11:55:30
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;


    /**
     * 社交登录
     */
    @PostMapping("/oauth2/login")
    public R oauth(@RequestBody SocialUserVo vo){


        MemberEntity entity = memberService.oauth(vo);

        if (entity !=null){

            return R.ok().setData(entity);
        }else {

            return   R.error(BizCodeEnume.LOGINCCT_PASSWORD_EXCETION.getCode(),BizCodeEnume.LOGINCCT_PASSWORD_EXCETION.getMsg());
        }



    }





    /**
     * 会员注册
     */
    @PostMapping("/regist")
    public R regist(@RequestBody MemberRegistVo memberRegistVo){

        Map<String, Object> map = memberService.savRegist(memberRegistVo);
        Object code = map.get("code");
        String key = code.toString();
        if (key.equals("1")){


            R.error(BizCodeEnume.PHONE_EXCETION.getCode(),BizCodeEnume.PHONE_EXCETION.getMsg());
        }
        if (key.equals("2")){


            R.error(BizCodeEnume.USER_EXCETION.getCode(),BizCodeEnume.USER_EXCETION.getMsg());
        }

        return R.ok();
    }

    /**
     * 登录
     * @param vo
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody MenberLoginVo vo){

       MemberEntity entity = memberService.login(vo);

       if (entity !=null){

           return R.ok().put("data",entity);
       }else {

         return   R.error(BizCodeEnume.LOGINCCT_PASSWORD_EXCETION.getCode(),BizCodeEnume.LOGINCCT_PASSWORD_EXCETION.getMsg());
       }

    }




    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
  //  @RequiresPermissions("gulimember:member:delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
