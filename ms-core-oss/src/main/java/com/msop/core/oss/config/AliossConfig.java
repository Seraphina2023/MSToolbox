package com.msop.core.oss.config;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.msop.core.oss.propertis.OssProperties;
import com.msop.core.oss.rule.MsOssRule;
import com.msop.core.oss.rule.OssRule;
import com.msop.core.oss.template.AliossTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 阿里云存储配置
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(QiniuConfig.class)
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(value = "oss.name", havingValue = "alioss")
public class AliossConfig {
    @Resource
    private OssProperties ossProperties;

    @Bean
    @ConditionalOnMissingBean(OssRule.class)
    public OssRule ossRule() {
        return new MsOssRule();
    }

    @Bean
    @ConditionalOnMissingBean(OSSClient.class)
    public OSSClient ossClient() {
        // 创建ClientConfiguration.ClientConfiguration是OSSClient的配置类，可配置带来、连接超时、最大连接数等参数
        ClientConfiguration configuration = new ClientConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个
        configuration.setMaxConnections(1024);
        // 设置Socket层传输时间的超市时间，默认为50000毫秒
        configuration.setSocketTimeout(50000);
        // 设置建立连接的超时时间，默认为50000毫秒
        configuration.setConnectionTimeout(50000);
        // 设置从连接池中获取连接的超时时间，默认不超时
        configuration.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒
        configuration.setIdleConnectionTime(60000);
        // 设置请求失败重试次数，默认为3次
        configuration.setMaxErrorRetry(5);
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(ossProperties.getAccessKey(), ossProperties.getSecretKey());
        return new OSSClient(ossProperties.getEndpoint(), credentialsProvider, configuration);
    }

    @Bean
    @ConditionalOnMissingBean(AliossTemplate.class)
    @ConditionalOnBean({OSSClient.class, OssRule.class})
    public AliossTemplate aliossTemplate(OSSClient ossClient, OssRule ossRule) {
        return new AliossTemplate();
    }

}
