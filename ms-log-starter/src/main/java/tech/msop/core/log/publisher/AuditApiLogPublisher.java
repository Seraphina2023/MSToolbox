package tech.msop.core.log.publisher;

import tech.msop.core.log.annotation.AuditLog;
import tech.msop.core.log.constant.EventConstant;
import tech.msop.core.log.event.AuditApiLogEvent;
import tech.msop.core.log.model.AuditApiLog;
import tech.msop.core.log.utils.AuditLogAbstractUtil;
import tech.msop.core.tool.constant.MsConstant;
import tech.msop.core.tool.utils.DateUtil;
import tech.msop.core.tool.utils.SpringUtil;
import tech.msop.core.tool.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * API审计日志事件发送
 *
 * @author ruozhuliufeng
 */
public class AuditApiLogPublisher {
    public static void publishEvent(String methodName, String methodClass, AuditLog auditLog, long time) {
        HttpServletRequest request = WebUtil.getRequest();
        AuditApiLog apiLog = new AuditApiLog();
        apiLog.setType(MsConstant.LOG_NORMAL_TYPE);
        apiLog.setTitle(auditLog.value());
        apiLog.setTime(String.valueOf(time));
        apiLog.setMethodClass(methodClass);
        apiLog.setMethodName(methodName);
        apiLog.setCreateTime(DateUtil.now());

        // 设置请求信息
        AuditLogAbstractUtil.addRequestInfoToLog(request, apiLog);
        Map<String, Object> event = new HashMap<>(16);
        event.put(EventConstant.EVENT_LOG, apiLog);
        SpringUtil.publishEvent(new AuditApiLogEvent(event));
    }
}
