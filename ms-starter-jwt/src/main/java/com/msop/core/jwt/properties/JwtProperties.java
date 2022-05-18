package com.msop.core.jwt.properties;

import com.msop.core.jwt.constant.JwtConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt 配置
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties("ms.token")
public class JwtProperties {
    /**
     * token 是否有状态
     */
    private Boolean state = Boolean.FALSE;
    /**
     * 是否只可同时在线一人
     */
    private Boolean single = Boolean.FALSE;

    /**
     * token 签名
     */
    private String signKey = JwtConstant.DEFAULT_SECRET_KEY;

    /**
     * 获取签名规则
     *
     * @return 签名
     */
    public String getSignKey() {
        if (this.signKey.length() < JwtConstant.SECRET_KEY_LENGTH) {
            return JwtConstant.DEFAULT_SECRET_KEY;
        }
        return this.signKey;
    }
}
