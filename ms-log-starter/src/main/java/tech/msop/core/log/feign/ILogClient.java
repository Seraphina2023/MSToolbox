package tech.msop.core.log.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tech.msop.core.launch.constant.AppConstant;
import tech.msop.core.log.model.AuditApiLog;
import tech.msop.core.log.model.AuditErrorLog;
import tech.msop.core.log.model.AuditUsualLog;
import tech.msop.core.tool.model.Result;

/**
 * Feign 日志接口类
 *
 * @author ruozhuliufeng
 */
@FeignClient(
        value = AppConstant.APPLICATION_LOG_NAME,
        fallback = LogClientFallback.class
)
public interface ILogClient {

    String API_PREFIX = "/log";

    /**
     * 保存审计操作日志
     * @param log 日志文件
     * @return 是否成功
     */
    @PostMapping(API_PREFIX + "/saveUsualLog")
    Result<Boolean> saveUsualLog(@RequestBody AuditUsualLog log);
    /**
     * 保存审计API日志
     * @param log 日志文件
     * @return 是否成功
     */
    @PostMapping(API_PREFIX + "/saveApiLog")
    Result<Boolean> saveApiLog(@RequestBody AuditApiLog log);
    /**
     * 保存审计错误日志
     * @param log 日志文件
     * @return 是否成功
     */
    @PostMapping(API_PREFIX + "/saveErrorLog")
    Result<Boolean> saveErrorLog(@RequestBody AuditErrorLog log);
}
