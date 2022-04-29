package com.msop.core.log.event;

import com.msop.core.log.constant.EventConstant;
import com.msop.core.log.model.AuditApiLog;
import com.msop.core.log.model.AuditUsualLog;
import com.msop.core.log.service.IAuditService;
import com.msop.core.log.utils.AuditLogAbstractUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 异步监听审计错误日志事件
 *
 * @author ruozhuliufeng
 */
@Slf4j
public class AuditUsualLogListener {
    @Resource
    private IAuditService auditService;
    @Value("${spring.application.name}")
    private String serviceId;
    @Async
    @Order
    @EventListener(AuditUsualLogEvent.class)
    public void saveAuditApiLog(AuditApiLogEvent event) {
        Map<String, Object> source = (Map<String, Object>) event.getSource();
        AuditUsualLog usualLog = (AuditUsualLog) source.get(EventConstant.EVENT_LOG);
        AuditLogAbstractUtil.addOtherInfoLog(usualLog,serviceId);
        auditService.saveAuditUsualLog(usualLog);
    }
}
