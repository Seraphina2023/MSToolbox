package tech.msop.core.log.service.impl;

import tech.msop.core.log.model.AuditApiLog;
import tech.msop.core.log.model.AuditErrorLog;
import tech.msop.core.log.model.AuditUsualLog;
import tech.msop.core.log.service.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 审计日志实现类-打印日志
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Service
@ConditionalOnProperty(value = "ms.audit.log.log-type", havingValue = "logger", matchIfMissing = true)
public class LoggerAuditServiceImpl implements IAuditService {

    /**
     * 打印接口日志信息，格式为{租户ID}|{链路ID}|{服务ID}|{服务IP}|{服务器名}|{服务器环境}|{日志类型}|{日志标题}|{操作方式}|{请求URI}|{用户代理}|{操作IP}|{方法类}|{方法名}|{操作提交的数据}|{执行时间}|{创建人}|{创建时间}
     */
    private static final String API_MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}";
    /**
     * 打印错误日志信息，格式为{租户ID}|{链路ID}|{服务ID}|{服务IP}|{服务器名}|{服务器环境}|{操作IP地址}|{请求URI}|{操作方式}|{方法类}|{方法名}|{操作提交的数据}|{执行时间}|{创建人}|{创建时间}|{堆栈信息}|{异常名}|{异常信息}|{文件名}|{代码行数}
     */
    private static final String ERROR_MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}";
    /**
     * 打印操作日志信息，格式为{租户ID}|{链路ID}|{服务ID}|{服务IP}|{服务器名}|{服务器环境}|{操作IP地址}|{请求URI}|{操作方式}|{方法类}|{方法名}|{操作提交的数据}|{执行时间}|{创建人}|{创建时间}|{日志级别}|{日志业务ID}|{日志数据}
     */
    private static final String USUAL_MSG_PATTERN = "{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}|{}";

    /**
     * 保存接口审计日志
     *
     * @param apiLog 接口日志信息
     */
    @Override
    public void saveAuditApiLog(AuditApiLog apiLog) {
        log.info(API_MSG_PATTERN, apiLog.getTenantId(), apiLog.getTraceId(), apiLog.getServiceId(), apiLog.getServerIp(), apiLog.getServerHost(),
                apiLog.getEnv(), apiLog.getType(), apiLog.getTitle(), apiLog.getMethod(), apiLog.getRequestUri(),
                apiLog.getUserAgent(), apiLog.getRemoteIp(), apiLog.getMethodClass(), apiLog.getMethodName(),
                apiLog.getParams(), apiLog.getTime(), apiLog.getCreateBy(), apiLog.getCreateTime());
    }

    /**
     * 保存错误审计日志
     *
     * @param errorLog 错误日志信息
     */
    @Override
    public void saveAuditErrorLog(AuditErrorLog errorLog) {
        log.error(ERROR_MSG_PATTERN, errorLog.getTenantId(), errorLog.getTraceId(), errorLog.getServiceId(),
                errorLog.getServerIp(), errorLog.getServerHost(),errorLog.getEnv(), errorLog.getRemoteIp(),
                errorLog.getRequestUri(), errorLog.getMethod(), errorLog.getMethodClass(), errorLog.getMethodName(),
                errorLog.getParams(), errorLog.getTime(), errorLog.getCreateBy(), errorLog.getCreateTime(),
                errorLog.getStackTrace(),errorLog.getExceptionName(), errorLog.getMessage(), errorLog.getFileName(), errorLog.getLineNumber());
    }

    /**
     * 保存操作审计日志
     *
     * @param usualLog 操作日志信息
     */
    @Override
    public void saveAuditUsualLog(AuditUsualLog usualLog) {
        log.info(USUAL_MSG_PATTERN,usualLog.getTenantId(), usualLog.getTraceId(), usualLog.getServiceId(),
                usualLog.getServerIp(), usualLog.getServerHost(),usualLog.getEnv(),usualLog.getRemoteIp(),
                usualLog.getRequestUri(), usualLog.getMethod(), usualLog.getMethodClass(), usualLog.getMethodName(),
                usualLog.getParams(), usualLog.getTime(), usualLog.getCreateBy(),
                usualLog.getCreateTime(), usualLog.getLogLevel(), usualLog.getLogId(), usualLog.getLogData());
    }


}
