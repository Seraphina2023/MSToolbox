package tech.msop.core.log.event;

import lombok.AllArgsConstructor;
import tech.msop.core.launch.properties.MsProperties;
import tech.msop.core.launch.server.ServerInfo;
import tech.msop.core.log.constant.EventConstant;
//import tech.msop.core.log.feign.ILogClient;
import tech.msop.core.log.model.AuditErrorLog;
import tech.msop.core.log.service.IAuditService;
import tech.msop.core.log.utils.AuditLogAbstractUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 异步监听审计错误日志事件
 *
 * @author ruozhuliufeng
 */
@Slf4j
@AllArgsConstructor
public class AuditErrorLogListener {
    private final IAuditService auditService;
    private final ServerInfo serverInfo;
    private final MsProperties msProperties;

    @Async
    @Order
    @EventListener(AuditErrorLogEvent.class)
    public void saveAuditApiLog(AuditApiLogEvent event) {
        Map<String, Object> source = (Map<String, Object>) event.getSource();
        AuditErrorLog errorLog = (AuditErrorLog) source.get(EventConstant.EVENT_LOG);
        AuditLogAbstractUtil.addOtherInfoLog(errorLog, msProperties, serverInfo);
//        logClient.saveErrorLog(errorLog);
        auditService.saveAuditErrorLog(errorLog);
    }
}
