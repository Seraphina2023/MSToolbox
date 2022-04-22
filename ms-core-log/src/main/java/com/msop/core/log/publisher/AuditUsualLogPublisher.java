package com.msop.core.log.publisher;

import com.msop.core.common.utils.SpringUtil;
import com.msop.core.common.utils.WebUtil;
import com.msop.core.log.constant.EventConstant;
import com.msop.core.log.event.AuditUsualLogEvent;
import com.msop.core.log.model.AuditUsualLog;
import com.msop.core.log.utils.AuditLogAbstractUtil;

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
