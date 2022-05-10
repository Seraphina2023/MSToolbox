package com.msop.core.log.utils;

import cn.hutool.core.util.ObjectUtil;
import com.msop.core.common.constant.StringConstant;
import com.msop.core.common.utils.DateUtil;
import com.msop.core.common.utils.UrlUtil;
import com.msop.core.common.utils.WebUtil;
import com.msop.core.log.model.AuditLogAbstract;
import com.msop.core.log.trace.MDCTraceUtils;
import com.msop.core.secure.util.AuthUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 审计日志工具类
 *
 * @author ruozhuliufeng
 */
public class AuditLogAbstractUtil {
    /**
     * log中添加补齐request的信息
     *
     * @param request     请求
     * @param logAbstract 审计日志
     */
    public static void addRequestInfoToLog(HttpServletRequest request, AuditLogAbstract logAbstract) {
        if (ObjectUtil.isNotEmpty(request)) {
            logAbstract.setRemoteIp(WebUtil.getIP(request));
            logAbstract.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
            logAbstract.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
            logAbstract.setMethod(request.getMethod());
            logAbstract.setParams(WebUtil.getRequestParamString(request));
            logAbstract.setCreateUser(String.valueOf(Objects.requireNonNull(AuthUtils.getUser(request)).getUserId()));
            logAbstract.setTenantId(request.getHeader("x-tenant-header"));
            logAbstract.setTraceId(request.getHeader(MDCTraceUtils.TRACE_ID_HEADER));
        }
    }

    public static void addOtherInfoLog(AuditLogAbstract logAbstract, String serviceId) {
        logAbstract.setServiceId(serviceId);
        logAbstract.setServerHost(INetUtil.getHostName());
        logAbstract.setServerIp(INetUtil.getHostIp());
        logAbstract.setCreateTime(DateUtil.now());
        if (ObjectUtil.isEmpty(logAbstract.getParams())) {
            logAbstract.setParams(StringConstant.EMPTY);
        }
    }
}
