package com.guli.common.constant;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/10
 */
public enum OrderStatusEnum {

        CREAT_NEW(0,"待付款"),
        PAYED(1,"已付款"),
        SENDED(2,"已发货"),
        RECIEVED(3,"已完成"),
        CANCLED(4,"已取消"),
        SERVICING(5,"售后中"),
        SERVICED(6,"售后完成");



       OrderStatusEnum (int coder, String msg){
               this.coder = coder;
               this.msg=msg;
       }


        private int coder;

        private String msg;


        public int getCoder(){

                return coder;
        }


        public String getMsg(){
                return msg;
        }


}
