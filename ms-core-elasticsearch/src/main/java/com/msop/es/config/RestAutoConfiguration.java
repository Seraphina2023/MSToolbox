package com.msop.es.config;

import com.msop.es.properties.RestClientPoolProperties;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

/**
 * ES自动配置
 *
 * @author ruozhuliufeng
 */
@EnableConfigurationProperties(RestClientPoolProperties.class)
public class RestAutoConfiguration {

    @Bean
    public RestClientBuilderCustomizer restClientBuilderCustomizer(RestClientPoolProperties poolProperties,
                                                                   ElasticsearchProperties restProperties) {
        return (builder) -> {
            setRequestConfig(builder, poolProperties);
            setHttpClientConfig(builder, poolProperties, restProperties);
        };
    }

    /**
     * 异步httpClient连接延时配置
     *
     * @param builder        参数
     * @param poolProperties 参数
     */
    private void setRequestConfig(RestClientBuilder builder, RestClientPoolProperties poolProperties) {
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(poolProperties.getConnectTimeOut())
                    .setSocketTimeout(poolProperties.getSocketTimeOut())
                    .setConnectionRequestTimeout(poolProperties.getConnectionRequestTimeOut());
            return requestConfigBuilder;
        });
    }

    /**
     * 异步httpClient连接数配置
     *
     * @param builder        参数
     * @param poolProperties 连接池参数
     * @param restProperties es参数
     */
    private void setHttpClientConfig(RestClientBuilder builder,
                                     RestClientPoolProperties poolProperties,
                                     ElasticsearchProperties restProperties) {
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(poolProperties.getMaxConnectNum())
                    .setMaxConnPerRoute(poolProperties.getMaxConnectPerRoute());
            PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
            map.from(restProperties::getUsername).to(username -> {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY,
                        new UsernamePasswordCredentials(username, restProperties.getPassword()));
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
            return httpClientBuilder;
        });
    }

    @Bean
    @ConditionalOnMissingBean
    public ElasticsearchRestTemplate elasticsearchRestTemplate(RestHighLevelClient restHighLevelClient) {
        return new ElasticsearchRestTemplate(restHighLevelClient);
    }
}
