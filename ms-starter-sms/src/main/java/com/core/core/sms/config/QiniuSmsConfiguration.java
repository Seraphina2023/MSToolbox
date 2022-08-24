package com.core.core.sms.config;

import com.core.core.sms.properties.SmsProperties;
import com.core.core.sms.template.QiniuSmsTemplate;
import com.msop.core.redis.cache.MsRedis;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云短信配置
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnClass(SmsManager.class)
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(value = "sms.name", havingValue = "qiniu")
public class QiniuSmsConfiguration {
    private final MsRedis msRedis;

    @Bean
    public QiniuSmsTemplate qiniuSmsTemplate(SmsProperties smsProperties) {
        Auth auth = Auth.create(smsProperties.getAccessKey(), smsProperties.getSecretKey());
        SmsManager smsManager = new SmsManager(auth);
        return new QiniuSmsTemplate(smsProperties, smsManager, msRedis);
    }
}
