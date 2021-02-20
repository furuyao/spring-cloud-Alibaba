package com.guli.common.to.mq;

import lombok.Data;

import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/11
 */
@Data
public class StockLockedTo {
        // 库存工作单ID
        private Long id;
        // 工作详情
       private StockDetailTo detail;



}
