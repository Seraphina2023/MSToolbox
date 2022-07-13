package com.msop.core.log.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 审计错误日志视图
 * @author ruozhuliufeng
 */
@Getter
@Setter
public class AuditErrorLogVO extends AuditErrorLog{
    private static final long serialVersionUID = 1L;
    private String strId;
}
