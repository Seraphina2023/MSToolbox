package com.msop.core.log.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 审计操作日志
 *
 * @author ruozhuliufeng
 */
@Setter
@Getter
public class AuditUsualLog extends AuditLogAbstract implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 日志级别
     */
    private String logLevel;
    /**
     * 日志业务ID
     */
    private String logId;
    /**
     * 日志数据
     */
    private String logData;
}
