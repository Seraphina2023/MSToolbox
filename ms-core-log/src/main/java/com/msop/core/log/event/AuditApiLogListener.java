package com.msop.core.log.event;

import com.msop.core.log.constant.EventConstant;
import com.msop.core.log.model.AuditApiLog;
import com.msop.core.log.service.IAuditService;
import com.msop.core.log.utils.AuditLogAbstractUtil;
import com.msop.launch.properties.MsProperties;
import com.msop.launch.server.ServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 异步监听审计API日志事件
 *
 * @author ruozhuliufeng
 */
@Slf4j
public class AuditApiLogListener {
    @Resource
    private IAuditService auditService;
    @Resource
    private ServerInfo serverInfo;
    @Resource
    private MsProperties msProperties;

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
