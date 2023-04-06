package tech.msop.core.log.utils;

import tech.msop.core.launch.properties.MsProperties;
import tech.msop.core.launch.server.ServerInfo;
import tech.msop.core.log.model.AuditLogAbstract;
import tech.msop.core.tool.constant.StringConstant;
import tech.msop.core.tool.utils.DateUtil;
import tech.msop.core.tool.utils.ObjectUtil;
import tech.msop.core.tool.utils.UrlUtil;
import tech.msop.core.tool.utils.WebUtil;
import javax.servlet.http.HttpServletRequest;

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
            logAbstract.setParams(WebUtil.getRequestContent(request));
            // TODO 创建人
//            logAbstract.setCreateBy(AuthUtil.getUserAccount(request));
            logAbstract.setTenantId(request.getHeader("x-tenant-header"));
        }
    }

    public static void addOtherInfoLog(AuditLogAbstract logAbstract, MsProperties msProperties, ServerInfo serverInfo) {
        logAbstract.setServiceId(msProperties.getName());
        logAbstract.setServerHost(serverInfo.getHostName());
        logAbstract.setServerIp(serverInfo.getIpWithPort());
        logAbstract.setCreateTime(DateUtil.now());
        if (ObjectUtil.isEmpty(logAbstract.getParams())) {
            logAbstract.setParams(StringConstant.EMPTY);
        }
    }
}
