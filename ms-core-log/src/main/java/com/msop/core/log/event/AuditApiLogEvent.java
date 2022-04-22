package com.msop.core.log.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 审计API日志事件
 *
 * @author ruozhuliufeng
 */
public class AuditApiLogEvent extends ApplicationEvent {
    public AuditApiLogEvent(Map<String, Object> source) {
        super(source);
    }
}
