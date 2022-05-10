package com.msop.core.common.propertis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Request 配置类
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties("ms.request")
public class MsRequestProperties {
    /**
     * 开启自定义request
     */
    private Boolean enabled = true;
    /**
     * 放行url
     */
    private List<String> skipUrl = new ArrayList<>();
}
