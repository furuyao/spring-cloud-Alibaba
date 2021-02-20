package com.guli.gulimember.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;
import com.guli.gulimember.entity.MemberEntity;
import com.guli.gulimember.vo.MemberRegistVo;
import com.guli.gulimember.vo.MenberLoginVo;
import com.guli.gulimember.vo.SocialUserVo;

import java.util.Map;

/**
 * 会员
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 11:55:30
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
    *@Author: fry
    *@Description: 注册会员
    *@Param: [memberRegistVo]
    *@Date: 2020/9/3 15:13
    */
    Map<String,Object> savRegist(MemberRegistVo memberRegistVo);

    /**
    *@Author: fry
    *@Description: 登录
    *@Param: [vo]
    *@Date: 2020/9/3 18:28
    */
    MemberEntity login(MenberLoginVo vo);

    /**
    *@Author: fry
    *@Description: 社交登录
    *@Param: [vo]
    *@Date: 2020/9/4 13:48
    */
    MemberEntity oauth(SocialUserVo vo);
}

