package com.guli.gulike.aoth.feign;

import com.guli.common.utils.R;
import com.guli.gulike.aoth.vo.SocialUserVo;
import com.guli.gulike.aoth.vo.UserLoginVo;
import com.guli.gulike.aoth.vo.UserRegistVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/3
 */
@FeignClient("guli-member")
public interface MenberFeignService {

    @PostMapping("/member/regist")
    R regist(@RequestBody UserRegistVo memberRegistVo);


    @PostMapping("/member/login")
    R login(@RequestBody UserLoginVo vo);
    /**
     * 社交登录
     */
    @PostMapping("/member/oauth2/login")
    R oauth(@RequestBody SocialUserVo vo);
}
