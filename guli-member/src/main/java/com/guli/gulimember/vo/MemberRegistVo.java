package com.guli.gulimember.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/3
 */

@Data
public class MemberRegistVo {
    //    用户名
    private String userName;
    // 密码
    private String password;
    //    手机号  ^[1]([3-9])[0-9]{9}$
    private String phone;
}
