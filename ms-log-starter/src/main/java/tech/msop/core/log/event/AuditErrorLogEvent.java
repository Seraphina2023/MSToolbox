package tech.msop.core.log.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * 审计错误日志事件
 *
 * @author ruozhuliufeng
 */
public class AuditErrorLogEvent extends ApplicationEvent {
    public AuditErrorLogEvent(Map<String, Object> source) {
        super(source);
    }
}
