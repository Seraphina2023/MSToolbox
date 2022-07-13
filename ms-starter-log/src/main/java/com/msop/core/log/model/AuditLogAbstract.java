package com.msop.core.log.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 审计日志公共父类
 *
 * @author ruozhuliufeng
 */
@Data
public class AuditLogAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 服务ID
     */
    protected String serviceId;
    /**
     * 服务器IP
     */
    protected String serverIp;
    /**
     * 服务器名
     */
    protected String serverHost;
    /**
     * 链路ID
     */
    protected String traceId;
    /**
     * 操作IP地址
     */
    protected String remoteIp;
    /**
     * 用户代理
     */
    protected String userAgent;
    /**
     * 请求URI
     */
    protected String requestUri;
    /**
     * 操作方式
     */
    protected String method;
    /**
     * 方法类
     */
    protected String methodClass;
    /**
     * 方法名
     */
    protected String methodName;
    /**
     * 操作提交的数据
     */
    protected String params;
    /**
     * 执行时长
     */
    protected String time;
    /**
     * 租户ID
     */
    protected String tenantId;
    /**
     * 创建人
     */
    protected String createUser;
    /**
     * 创建时间
     */
    protected Date createTime;

}
