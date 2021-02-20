package com.guli.gulike.aoth.vo;

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
public class UserRegistVo {
//    用户名
    @NotEmpty(message = "用户名必须提交")
    @Length(min = 6, max = 18,message = "密码必须6-18位")
    private String userName;
    // 密码
    @NotEmpty(message = "密码必须填写")
    @Length(min = 6, max = 18,message = "密码必须6-18位")
    private String password;
//    手机号  ^[1]([3-9])[0-9]{9}$
    @NotEmpty(message = "手机号必须填写")
    @Pattern(regexp = "^1(3[0-9]|5[189]|8[6789])[0-9]{8}$",message = "手机格式不正确")
    private String phone;
    // 验证吗
    @NotEmpty(message = "验证码必须填写")
    private String code;



}
