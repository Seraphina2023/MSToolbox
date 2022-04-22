package com.msop.core.log.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 审计操作日志事件
 *
 * @author ruozhuliufeng
 */
public class AuditUsualLogEvent extends ApplicationEvent {
    public AuditUsualLogEvent(Map<String, Object> source) {
        super(source);
    }
}
