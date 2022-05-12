package com.msop.core.secure.provider;

import lombok.Data;

@Data
public class ClientDetails implements IClientDetails {
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String clientSecret;
    /**
     * 令牌过期秒数
     */
    private Integer accessTokenValidity;
    /**
     * 刷新令牌过期秒数
     */
    private Integer refreshTokenValidity;

}
