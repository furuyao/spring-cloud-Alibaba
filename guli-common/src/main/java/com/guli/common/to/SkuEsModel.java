package com.guli.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/24
 */
@Data
public class SkuEsModel {

        private Long skuId;

        private Long spuId;

        private String skuTitle;

        private BigDecimal skuPrice;

        private String skuImg;

        private Long saleCount;

        private Boolean hasStock;

        private Long hotScore;

        private Long brandId;

        private Long catalogId;

        private String brandName;

        private String brandImg;

        private String catalogName;

        private List<Attrs> attrs;
        @Data
       public static class Attrs{

        private Long attrId;
        private String attrName;
        private String attrValue;
}



}
