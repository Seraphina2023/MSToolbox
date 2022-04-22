package com.msop.core.log.publisher;

import com.msop.core.common.constant.MsConstant;
import com.msop.core.common.utils.SpringUtil;
import com.msop.core.common.utils.WebUtil;
import com.msop.core.log.annotation.AuditLog;
import com.msop.core.log.constant.EventConstant;
import com.msop.core.log.event.AuditApiLogEvent;
import com.msop.core.log.model.AuditApiLog;
import com.msop.core.log.utils.AuditLogAbstractUtil;

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

        // 设置请求信息
        AuditLogAbstractUtil.addRequestInfoToLog(request, apiLog);
        Map<String, Object> event = new HashMap<>(16);
        event.put(EventConstant.EVENT_LOG, apiLog);
        SpringUtil.publishEvent(new AuditApiLogEvent(event));
    }
}
