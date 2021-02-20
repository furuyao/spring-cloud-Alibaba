package com.guli.gulielasticsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.guli.common.to.SkuEsModel;
import com.guli.gulielasticsearch.constant.EsConstant;
import com.guli.gulielasticsearch.config.GulimallElasticSearchConfig;
import com.guli.gulielasticsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/25
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {


    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {

        // 组装数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            String key = JSON.toJSONString(model);
            // 指定数据类型
            indexRequest.source(key, XContentType.JSON);
            bulkRequest.add(indexRequest);

        }

        // 保存到Es
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        boolean b = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map(itm -> {
            return itm.getId();

        }).collect(Collectors.toList());

        log.info("商品上架成功{}", collect);
    return b;
    }
}
