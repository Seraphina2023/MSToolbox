package com.core.core.sms.config;

import com.core.core.sms.properties.SmsProperties;
import com.core.core.sms.template.YunpianSmsTemplate;
import com.msop.core.redis.cache.MsRedis;
import com.yunpian.sdk.YunpianClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 云片短信配置
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnClass(YunpianClient.class)
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(value = "sms.name", havingValue = "yunpian")
public class YunpianSmsConfiguration {
    private final MsRedis msRedis;

    @Bean
    public YunpianSmsTemplate yunpianSmsTemplate(SmsProperties smsProperties) {
        YunpianClient client = new YunpianClient(smsProperties.getAccessKey()).init();
        return new YunpianSmsTemplate(smsProperties, client, msRedis);
    }
}
