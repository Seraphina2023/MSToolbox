package com.msop.core.tool.propertis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Xss配置类
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties("ms.xss")
public class MsXssProperties {
    /**
     * 是否开启xss
     */
    private Boolean enabled = true;
    /**
     * 放行url
     */
    private List<String> skipUrl = new ArrayList<>();
}
