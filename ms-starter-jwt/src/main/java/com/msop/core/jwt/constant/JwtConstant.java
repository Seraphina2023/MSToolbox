package com.msop.core.jwt.constant;

/**
 * Jwt 常量配置
 */
public interface JwtConstant {

    /**
     * 默认key
     */
    String DEFAULT_SECRET_KEY = "mstoolboxisapowerfulmicroservicetool";
    /**
     * key 安全长度，具体见：<a href="https://tools.ietf.org/html/rfc7518#section-3.2">...</a>
     */
    int SECRET_KEY_LENGTH = 32;
}
