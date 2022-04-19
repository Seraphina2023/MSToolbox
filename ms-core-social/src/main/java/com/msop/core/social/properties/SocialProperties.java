package com.msop.core.social.properties;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthDefaultSource;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;


/**
 * 第三方登录配置信息
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ms.social")
public class SocialProperties {
    /**
     * 是否启用
     */
    private Boolean enabled = false;
    /**
     * 域名地址
     */
    private String domain;
    /**
     * 类型
     */
    private Map<AuthDefaultSource, AuthConfig> oauth = Maps.newHashMap();
    /**
     * 别名
     */
    private Map<String, String> alias = Maps.newHashMap();
}
