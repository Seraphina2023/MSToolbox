package com.core.core.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 云短信配置
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 短信服务名称
     */
    private String name;

    /**
     * 短信模板ID
     */
    private String templateId;

    /**
     * regionId
     */
    private String regionId = "cn-hangzhou";

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 短信签名
     */
    private String signName;
}
