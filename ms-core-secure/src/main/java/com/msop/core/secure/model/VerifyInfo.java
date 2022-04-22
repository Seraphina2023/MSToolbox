package com.msop.core.secure.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 校验信息
 *
 * @author ruozhuliufeng
 */
@Data
public class VerifyInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 请求方式 (GET/POST/PUT/DELETE等)
     */
    private String pathMethod;
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 请求地址
     */
    private String requestUrl;
}
