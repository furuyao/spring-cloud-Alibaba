package com.guli.common.constant;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/20
 */
public class WareConstant {
    public enum PurchaseEnum{
        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        RECEIVE(2,"已领取"),
        FINISH(3,"已完成"),
        HASERROR(4,"有异常");

        private int code;

        private String msg;

        PurchaseEnum(int code, String msg){
            this.code = code;
            this.msg = msg;

        }
        public int getCode(){
            return code;
        }

        public String getMsg(){
            return msg;
        }

    }


    public enum PurchaseDateEnum{
        CREATED(0,"新建"),
        ASSIGNED(1,"已分配"),
        BUYING(2,"正在采购"),
        FINISH(3,"已完成"),
        HASERROR(4,"采购失败");

        private int code;

        private String msg;

        PurchaseDateEnum(int code, String msg){
            this.code = code;
            this.msg = msg;

        }
        public int getCode(){
            return code;
        }

        public String getMsg(){
            return msg;
        }

    }

}
