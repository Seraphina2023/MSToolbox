package com.msop.core.loadbalancer;

import com.msop.core.loadbalancer.config.RestTemplateProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 模板自动配置
 *
 * @author ruozhuliufeng
 */
@EnableConfigurationProperties(RestTemplateProperties.class)
public class RestTemplateAutoConfiguration {
    @Autowired
    private RestTemplateProperties properties;

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(httpRequestFactory);
        return restTemplate;
    }

    /**
     * httpClient 实现的ClientHttpRequestFactory
     *
     * @return ClientHttpRequestFactory
     */
    @Bean
    public ClientHttpRequestFactory httpRequestFactory(HttpClient client) {
        return new HttpComponentsClientHttpRequestFactory(client);
    }

    /**
     * 使用连接池的httpClient
     *
     * @return HttpClient
     */
    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        // 最大连接数
        connectionManager.setMaxTotal(properties.getMaxTotal());
        // 同路由最大并发数 20
        connectionManager.setDefaultMaxPerRoute(properties.getMaxPerRoute());

        RequestConfig requestConfig = RequestConfig.custom()
                // 读超时
                .setSocketTimeout(properties.getReadTimeout())
                // 连接超时
                .setConnectTimeout(properties.getCnnectTimeout())
                // 连接不够用的等待时间
                .setConnectionRequestTimeout(properties.getReadTimeout())
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .build();
    }
}
