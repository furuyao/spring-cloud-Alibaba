package com.guli.giliorder.vo;

import com.guli.giliorder.entity.OrderEntity;
import lombok.Data;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/9
 */
@Data
public class SubmitOderResponseVo {


        private OrderEntity order;
        // 状态码 不是0为成功
        private  Integer coder;



}
