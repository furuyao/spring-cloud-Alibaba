package com.guli.common.constant;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/16
 */
public class ProductConstant {

    public enum AttrEnum{
        ATTR_TYPE_BASE(1,"基本属性"),
        ATTR_TYPE_SALE(0,"销售属性");

        private int code;

        private String msg;

        AttrEnum(int code, String msg){
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

    public enum StartsEnum{
        NEW_SPU(0,"新建"),
        ATTR_TYPE_SALE(1,"商品上架"),
        SPU_DOWN(2,"商品下级");

        private int code;

        private String msg;

        StartsEnum(int code, String msg){
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
