package com.msop.core.log.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 审计API日志实体实体
 * @author ruozhuliufeng
 */
@Getter
@Setter
public class AuditApiLogVO extends AuditApiLog {
    private static final long serialVersionUID = 1L;
    private String strId;
}
