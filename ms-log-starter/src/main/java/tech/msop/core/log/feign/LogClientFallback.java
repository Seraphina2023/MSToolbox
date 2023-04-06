package tech.msop.core.log.feign;

import org.springframework.stereotype.Component;
import tech.msop.core.log.model.AuditApiLog;
import tech.msop.core.log.model.AuditErrorLog;
import tech.msop.core.log.model.AuditUsualLog;
import tech.msop.core.tool.model.Result;

/**
 * 日志 fallback
 *
 * @author ruozhuliufeng
 */
@Component
public class LogClientFallback implements ILogClient {
    /**
     * 保存审计操作日志
     *
     * @param log 日志文件
     * @return 是否成功
     */
    @Override
    public Result<Boolean> saveUsualLog(AuditUsualLog log) {
        return Result.failed("usual log send fail");
    }

    /**
     * 保存审计API日志
     *
     * @param log 日志文件
     * @return 是否成功
     */
    @Override
    public Result<Boolean> saveApiLog(AuditApiLog log) {
        return Result.failed("api log send fail");
    }

    /**
     * 保存审计错误日志
     *
     * @param log 日志文件
     * @return 是否成功
     */
    @Override
    public Result<Boolean> saveErrorLog(AuditErrorLog log) {
        return Result.failed("error log send fail");
    }
}
