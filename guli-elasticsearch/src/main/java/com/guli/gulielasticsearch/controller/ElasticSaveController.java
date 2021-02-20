package com.guli.gulielasticsearch.controller;

import com.guli.common.exception.BizCodeEnume;
import com.guli.common.to.SkuEsModel;
import com.guli.common.utils.R;
import com.guli.gulielasticsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/24
 */
@Slf4j
@RestController
@RequestMapping("/search/save")
public class ElasticSaveController {

    @Autowired
    ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStartusUp(@RequestBody List<SkuEsModel> skuEsModels) {

        boolean b = false;
        try {
            b = productSaveService.productStatusUp(skuEsModels);
        } catch (IOException e) {
            log.info("商品上架失败：{}",e);
            return R.error(BizCodeEnume.PRODUCT_UP_UNKNOW_EXCETION.getCode(),BizCodeEnume.PRODUCT_UP_UNKNOW_EXCETION.getMsg());
        }
        if (!b){
            return R.ok();
        }else {
            log.info("商品上架失败：{}");
            return R.error(BizCodeEnume.PRODUCT_UP_UNKNOW_EXCETION.getCode(),BizCodeEnume.PRODUCT_UP_UNKNOW_EXCETION.getMsg());
        }
    }

}
