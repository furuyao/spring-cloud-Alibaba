package com.guli.gulielasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/8/23
 */
@Configuration
public class GulimallElasticSearchConfig {

        public  static final RequestOptions COMMON_OPTIONS;

        static {
            RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

            COMMON_OPTIONS=builder.build();

        }



        @Bean
        public RestHighLevelClient esRestClient(){

            RestClientBuilder restClient = RestClient.builder(new HttpHost("192.168.37.134", 9200, "http"));

            RestHighLevelClient  levelClient = new RestHighLevelClient(restClient);
            return levelClient;
        }

}
