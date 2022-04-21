package com.msop.core.secure.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 认证配置
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
public class AuthProperties {

    /**
     * 配置需要认证的url(默认不需要配置)，优先级大于忽略认证配置
     * `ossp.security.ignore.httpUrls`，
     * 如果同一个url同时配置了忽略认证和需要认证，则该url还是会被认证
     */
    private String[] httpUrls = {};

    /**
     * token自动续签配置(暂时只有redis实现)
     */
    private RenewProperties renew = new RenewProperties();

    /**
     * url权限配置
     */
    private UrlPermissionProperties urlPermission = new UrlPermissionProperties();

    /**
     * 是否开启统一登出<br>
     * 1. 登出时把同一个用户名下的所有token都注销<br>
     * 2. 登出信息通知所有的单点登录系统
     */
    private Boolean unifiedLogout = false;
    /**
     * 是否同应用同账号登录互踢
     */
    private Boolean isSingleLogin = false;
    /**
     * 是否同应用同账号登录时共用token
     * true: 多个用户使用同一账号登录时共用一个token
     * false: 使用同一账号会新建一个token
     */
    private Boolean isShareToken = true;
}
