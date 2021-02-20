package com.guli.gulielasticsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.guli.common.to.SkuEsModel;
import com.guli.gulielasticsearch.config.GulimallElasticSearchConfig;
import com.guli.gulielasticsearch.constant.EsConstant;
import com.guli.gulielasticsearch.service.MallSearchService;
import com.guli.gulielasticsearch.vo.SearchParam;
import com.guli.gulielasticsearch.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @创建人: fry
 * @用于： 页面检索
 * @创建时间 2020/8/28
 */
@Service
public class MallSearchServicelmpl implements MallSearchService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResult selectSearch(SearchParam searchParam) {
        SearchResult searchResult = null;
        // 1.动态构建DSL语句


        // 1.准备检索
        SearchRequest searchRequest = buildSearchRequest(searchParam);


        //2.执行检索
        try {
            SearchResponse search = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
            // 3.把返回数据封装成返回数据
            searchResult = buildSearchretun(search, searchParam);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResult;
    }

    /**
     * 构建返回数据
     */
    private SearchResult buildSearchretun(SearchResponse search, SearchParam searchParam) {

        SearchHits hits = search.getHits();


        SearchResult searchResult = new SearchResult();
        List<SkuEsModel> skuEsModels = new ArrayList<>();

        if (hits.getHits() != null) {
            for (SearchHit his : hits.getHits()) {
                String sourceAsString = his.getSourceAsString();
                SkuEsModel model = JSON.parseObject(sourceAsString, SkuEsModel.class);

                if (!StringUtils.isEmpty(searchParam.getKeyword())) {

                    HighlightField skuTitle = his.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    // 设置高亮
                    model.setSkuTitle(string);
                }
                skuEsModels.add(model);
            }

        }
        searchResult.setProducts(skuEsModels);

        //当前所有商品涉及到的所有分类信息
        ParsedStringTerms catalog_agg = search.getAggregations().get("catalog_agg");
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        List<SearchResult.CatalogVO> catalogVOS = new ArrayList<>();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVO catalogVO = new SearchResult.CatalogVO();
            // 得到分类ID
            String keyAsString = bucket.getKeyAsString();
            catalogVO.setCatalogId(Long.parseLong(keyAsString));
            // 得到分类名
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String asString = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVO.setCatalogName(asString);
            catalogVOS.add(catalogVO);
        }
        searchResult.setCatalogs(catalogVOS);

        //当前所有商品涉及到的属性信息
        List<SearchResult.AttrVO> attrVOS = new ArrayList<>();
        ParsedNested attr_agg = search.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket terms : attr_id_agg.getBuckets()) {
            SearchResult.AttrVO attrVO = new SearchResult.AttrVO();

            // 得到属性id
            long l = terms.getKeyAsNumber().longValue();

            //得到属性名字
            String attrNameAgg = "";
            if (((ParsedStringTerms) terms.getAggregations().get("attr_name_agg")).getBuckets().size() > 0) {
                attrNameAgg = ((ParsedStringTerms) terms.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            }
            //得到属性的所有值
            List<String> attrValueAgg = ((ParsedStringTerms) terms.getAggregations().get("attr_value_agg")).getBuckets().stream().map(itm -> {
                String keyAsString = ((Terms.Bucket) itm).getKeyAsString();
                return keyAsString;
            }).collect(Collectors.toList());
            attrVO.setAttrId(l);
            attrVO.setAttrName(attrNameAgg);
            attrVO.setAttrValue(attrValueAgg);
            attrVOS.add(attrVO);
        }


        searchResult.setAttrs(attrVOS);
        // 当前所有商品涉及到的品牌信息
        List<SearchResult.BrandVO> brandVOS = new ArrayList<>();
        ParsedLongTerms brand_agg = search.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVO brandVO = new SearchResult.BrandVO();
            // 得到品牌ID
            long value = bucket.getKeyAsNumber().longValue();
            // 品牌的名字

            String brandNameAgg = "";
            if (((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().size() > 0) {
                brandNameAgg = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();

            }
            String brandImgAgg = "";
            if (((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().size() > 0) {
                brandImgAgg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();

            }
            // 品牌的图片
            brandVO.setBrandId(value);
            brandVO.setBrandImg(brandImgAgg);
            brandVO.setBrandName(brandNameAgg);
            brandVOS.add(brandVO);
        }

        searchResult.setBrands(brandVOS);

        // 分页信息
        searchResult.setPageNum(searchParam.getPageNum());
        // 分页信息 总记录数
        long total = hits.getTotalHits().value;
        searchResult.setTotalRecords(total);

        //总页码
        int page = (int) total % EsConstant.PRODUCT_PAGESIZE == 0 ? (int) total / EsConstant.PRODUCT_PAGESIZE : ((int) total / EsConstant.PRODUCT_PAGESIZE + 1);
        searchResult.setTotalPages(page);

        List<Integer> integers = new ArrayList<>();

        for (int i = 1; i <= page; i++) {
            integers.add(i);

        }
        searchResult.setPageNavs(integers);

        return searchResult;
    }

    /**
     * 构建条件查询
     */

    private SearchRequest buildSearchRequest(SearchParam param) {
        // 只构建查询条件
        SearchSourceBuilder searchSource = new SearchSourceBuilder();
        // 构建一个boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }

        if (param.getCatalog3Id() != null) {
            // 按照三级分类ID进行过滤
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));

        }
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            // 根据品牌ID查询
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));

        }
        if (param.getAttrs() != null) {


            for (String key : param.getAttrs()) {
                BoolQueryBuilder builder = QueryBuilders.boolQuery();
                // attrs= 1_5寸：8寸
                String[] s = key.split("_");
                String attrId = s[0]; // 属性Id
                String[] split = s[1].split(":");// 属性值
                builder.must(QueryBuilders.termsQuery("attrs.attrId", attrId));
                builder.must(QueryBuilders.termsQuery("attrs.attrValue", split));
                // 构建嵌入式查询    因为是一个集合所以集合里面的每一个都要构建成一个查询条件
                NestedQueryBuilder attrs = QueryBuilders.nestedQuery("attrs", builder, ScoreMode.None);
                boolQueryBuilder.filter(attrs);
            }
        }
        //

        TermsQueryBuilder hasStock = QueryBuilders.termsQuery("hasStock", param.getHasStock() == 0);
        boolQueryBuilder.filter(hasStock);


        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            // 拼装区间查询
            RangeQueryBuilder skuPrice = QueryBuilders.rangeQuery("skuPrice");
            String[] s = param.getSkuPrice().split("_");
            if (s.length == 2) {
                // 设置区间查询
                skuPrice.gte(s[0]).lte(s[1]);

            } else if (s.length == 1) {
                // 如果只有一个(_)就继续判断是大于还是小于
                if (param.getSkuPrice().startsWith("_")) {
                    skuPrice.lte(s[0]);
                }
                if (param.getSkuPrice().endsWith("_")) {

                    skuPrice.gte(s[0]);

                }

            }


        }

        searchSource.query(boolQueryBuilder);

        /**
         * 排序，分页，高亮
         */
        // 排序
        if (!StringUtils.isEmpty(param.getSort())) {
            String sort = param.getSort();
            // 分割0是根据那个字段排序，1是升序还是降序
            String[] s = sort.split("_");
            SortOrder sortOrder = s[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;

            searchSource.sort(s[0], sortOrder);

        }

        // 分页
        searchSource.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        searchSource.size(EsConstant.PRODUCT_PAGESIZE);

        // 高亮  有模糊查询才需要高亮

        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();

            builder.field("skuTitle");
            // 设置页面
            builder.postTags("</b>");
            builder.preTags("<b style='color:red'>");
            searchSource.highlighter(builder);

        }

        /**
         * 聚合分析
         */

        // 品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);


        // 品牌聚合 的子聚合
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        // TODO 1聚合brand
        searchSource.aggregation(brand_agg);


        // 分类聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        catalog_agg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));

        // TODO 2 聚合catalog
        searchSource.aggregation(catalog_agg);


        // 属性聚合
        NestedAggregationBuilder attr_agg = AggregationBuilders.nested("attr_agg", "attrs");
        // 聚合出当前的所有attrId
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        // 聚合分析当前attr_id
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));

        // 聚合分析出当前attr_id对应的所以可能的属性值
        attr_id_agg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));

        attr_agg.subAggregation(attr_id_agg);
        // TODO 3 聚合attr_agg
        searchSource.aggregation(attr_agg);


        System.out.println(searchSource.toString());

        // 把条件构建成类（包括要查的索引）
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSource);

        return searchRequest;
    }
}
