package com.msop.core.oss.propertis;

import com.msop.core.common.support.Kv;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 参数配置类
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties(prefix = "ms.oss")
public class OssProperties {
    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 对象存储名称
     */
    private String name;
    /**
     * 是否开启租户模式
     */
    private Boolean tenantMode;
    /**
     * 访问端点，对象存储服务的URL
     */
    private String endpoint;
    /**
     * 唯一标识
     */
    private String accessKey;
    /**
     * 访问密钥
     */
    private String secretKey;
    /**
     * 默认的存储桶名称
     */
    private String bucketName;
    /**
     * 自定义属性信息
     */
    private Kv args;
}
