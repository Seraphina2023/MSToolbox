package com.msop.core.social.config;

import com.msop.core.social.properties.SocialProperties;
import com.xkcoding.http.HttpUtil;
import com.xkcoding.http.support.Http;
import com.xkcoding.http.support.httpclient.HttpClientImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 第三方登录配置
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SocialProperties.class)
public class SocialConfig {

    @Bean
    @ConditionalOnMissingBean(Http.class)
    public Http simpleHttp(){
        HttpClientImpl httpClient = new HttpClientImpl();
        HttpUtil.setHttp(httpClient);
        return httpClient;
    }
}
