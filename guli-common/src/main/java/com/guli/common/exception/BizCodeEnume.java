package com.guli.common.exception;

public enum  BizCodeEnume {

    // 11 商品
    // 12 订单
    // 13 购物车
    // 14 物流
    // 15 用户
    // 21 库存

    VAILD_EXCEPTION(10001,"参数格式校验失败"),
    SMS_CODE_EXCETION(10002,"验证码获取频率过高，请稍后在试"),
    UNKNOW_EXCETION(10000,"系统未知异常"),
    TO_MANY_REQUEST(310000,"请求流量过大"),
    USER_EXCETION(15001,"用户已存在"),
    PHONE_EXCETION(15002,"手机号已被注册"),
    LOGINCCT_PASSWORD_EXCETION(15003,"账号或密码错误"),
    NO_STOC_EXCETION(21000,"商品库存不足"),
    PRODUCT_UP_UNKNOW_EXCETION(11000,"商品上架失败");

    private int code;
    private String msg;
    BizCodeEnume(int code , String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
