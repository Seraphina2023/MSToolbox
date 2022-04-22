package com.msop.core.secure.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体
 *
 * @author ruozhuliufeng
 */
@Data
public class MsUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 客户端ID
     */
    private String clientId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 账户
     */
    private String account;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 角色名
     */
    private String roleName;
}
