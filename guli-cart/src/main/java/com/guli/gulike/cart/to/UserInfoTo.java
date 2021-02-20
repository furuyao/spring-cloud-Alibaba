package com.guli.gulike.cart.to;

import lombok.Data;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/6
 */
@Data
public class UserInfoTo {
    // 用户ID
    private Long userId;
    // 登录后的Cookie
    private String userKey;
    // 是否有临时用户

    private boolean tempUser=false;


}
