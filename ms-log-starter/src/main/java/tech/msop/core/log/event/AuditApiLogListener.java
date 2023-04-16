package tech.msop.core.log.event;

import lombok.AllArgsConstructor;
import tech.msop.core.launch.properties.MsProperties;
import tech.msop.core.launch.server.ServerInfo;
import tech.msop.core.log.constant.EventConstant;
import tech.msop.core.log.model.AuditApiLog;
import tech.msop.core.log.service.IAuditService;
import tech.msop.core.log.utils.AuditLogAbstractUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 异步监听审计API日志事件
 *
 * @author ruozhuliufeng
 */
@Slf4j
@AllArgsConstructor
public class AuditApiLogListener {
    private final IAuditService auditService;
    private final ServerInfo serverInfo;
    private final MsProperties msProperties;

    @Async
    @Order
    @EventListener(AuditApiLogEvent.class)
    public void saveAuditApiLog(AuditApiLogEvent event) {
        Map<String, Object> source = (Map<String, Object>) event.getSource();
        AuditApiLog apiLog = (AuditApiLog) source.get(EventConstant.EVENT_LOG);
        AuditLogAbstractUtil.addOtherInfoLog(apiLog, msProperties, serverInfo);
        auditService.saveAuditApiLog(apiLog);
    }
}
