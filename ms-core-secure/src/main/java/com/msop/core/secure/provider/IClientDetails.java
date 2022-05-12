package com.msop.core.secure.provider;

import java.io.Serializable;

/**
 * 多终端详情接口
 *
 * @author ruozhuliufeng
 */
public interface IClientDetails extends Serializable {

    /**
     * 客户端ID
     *
     * @return 客户端ID
     */
    String getClientId();

    /**
     * 客户端密钥
     *
     * @return 客户端密钥
     */
    String getClientSecret();

    /**
     * 客户端Token过期时间
     *
     * @return 过期时间
     */
    Integer getAccessTokenValidity();

    /**
     * 客户端刷新Token过期时间
     *
     * @return 过期时间
     */
    Integer getRefreshTokenValidity();
}
