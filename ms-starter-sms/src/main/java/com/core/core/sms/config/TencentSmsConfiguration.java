package com.core.core.sms.config;

import com.core.core.sms.properties.SmsProperties;
import com.core.core.sms.template.TencentSmsTemplate;
import com.github.qcloudsms.SmsMultiSender;
import com.msop.core.redis.cache.MsRedis;
import com.msop.core.tool.utils.Func;
import com.qiniu.util.Auth;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云短信配置
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnClass(SmsMultiSender.class)
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(value = "sms.name", havingValue = "tencent")
public class TencentSmsConfiguration {
    private final MsRedis msRedis;

    @Bean
    public TencentSmsTemplate tencentSmsTemplate(SmsProperties smsProperties) {
        SmsMultiSender smsSender = new SmsMultiSender(Func.toInt(smsProperties.getAccessKey()), smsProperties.getSecretKey());
        return new TencentSmsTemplate(smsProperties, smsSender, msRedis);
    }
}
