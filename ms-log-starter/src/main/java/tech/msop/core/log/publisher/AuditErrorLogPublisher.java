package tech.msop.core.log.publisher;

import tech.msop.core.tool.utils.Exceptions;
import tech.msop.core.tool.utils.Func;
import tech.msop.core.tool.utils.SpringUtil;
import tech.msop.core.tool.utils.WebUtil;
import tech.msop.core.log.constant.EventConstant;
import tech.msop.core.log.event.AuditErrorLogEvent;
import tech.msop.core.log.model.AuditErrorLog;
import tech.msop.core.log.utils.AuditLogAbstractUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 审计错误日志事件发送
 *
 * @author ruozhuliufeng
 */
public class AuditErrorLogPublisher {

    public static void publishEvent(Throwable error, String requestUri) {
        HttpServletRequest request = WebUtil.getRequest();
        AuditErrorLog errorLog = new AuditErrorLog();
        errorLog.setRequestUri(requestUri);
        if (Func.isNotEmpty(error)) {
            errorLog.setStackTrace(Exceptions.getStackTraceAsString(error));
            errorLog.setExceptionName(error.getClass().getName());
            errorLog.setMessage(error.getMessage());
            StackTraceElement[] elements = error.getStackTrace();
            if (Func.isNotEmpty(elements)) {
                StackTraceElement element = elements[0];
                errorLog.setMethodName(element.getMethodName());
                errorLog.setMethodClass(element.getClassName());
                errorLog.setFileName(element.getFileName());
                errorLog.setLineNumber(element.getLineNumber());
            }
        }
        AuditLogAbstractUtil.addRequestInfoToLog(request, errorLog);

        // 事件发送
        Map<String, Object> event = new HashMap<>(16);
        event.put(EventConstant.EVENT_LOG, errorLog);
        event.put(EventConstant.EVENT_REQUEST, request);
        SpringUtil.publishEvent(new AuditErrorLogEvent(event));
    }
}
