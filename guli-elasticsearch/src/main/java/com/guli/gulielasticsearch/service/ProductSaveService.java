package com.guli.gulielasticsearch.service;

import com.guli.common.to.SkuEsModel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/25
 */
@Component
public interface ProductSaveService {

    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
