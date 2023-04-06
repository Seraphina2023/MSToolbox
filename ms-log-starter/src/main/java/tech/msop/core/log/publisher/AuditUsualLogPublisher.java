package tech.msop.core.log.publisher;

import tech.msop.core.tool.utils.SpringUtil;
import tech.msop.core.tool.utils.WebUtil;
import tech.msop.core.log.constant.EventConstant;
import tech.msop.core.log.event.AuditUsualLogEvent;
import tech.msop.core.log.model.AuditUsualLog;
import tech.msop.core.log.utils.AuditLogAbstractUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 审计操作日志事件发布
 *
 * @author ruozhuliufeng
 */
public class AuditUsualLogPublisher {

    public static void publishEvent(String level,String id,String data){
        HttpServletRequest request = WebUtil.getRequest();
        AuditUsualLog usualLog = new AuditUsualLog();
        usualLog.setLogLevel(level);
        usualLog.setLogId(id);
        usualLog.setLogData(data);

        AuditLogAbstractUtil.addRequestInfoToLog(request,usualLog);

        Map<String,Object> event = new HashMap<>(16);
        event.put(EventConstant.EVENT_LOG,usualLog);
        event.put(EventConstant.EVENT_REQUEST,request);
        SpringUtil.publishEvent(new AuditUsualLogEvent(event));
    }
}
