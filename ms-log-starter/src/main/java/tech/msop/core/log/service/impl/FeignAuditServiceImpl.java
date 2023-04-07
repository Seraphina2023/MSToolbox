package tech.msop.core.log.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import tech.msop.core.log.feign.ILogClient;
import tech.msop.core.log.model.AuditApiLog;
import tech.msop.core.log.model.AuditErrorLog;
import tech.msop.core.log.model.AuditUsualLog;
import tech.msop.core.log.service.IAuditService;

@Service
@ConditionalOnProperty(value = "ms.audit.log.log-type", havingValue = "feign")
@AllArgsConstructor
public class FeignAuditServiceImpl implements IAuditService {
    private final ILogClient logClient;

    /**
     * 保存接口审计日志
     *
     * @param apiLog 接口日志信息
     */
    @Override
    public void saveAuditApiLog(AuditApiLog apiLog) {
        logClient.saveApiLog(apiLog);
    }

    /**
     * 保存错误审计日志
     *
     * @param errorLog 错误日志信息
     */
    @Override
    public void saveAuditErrorLog(AuditErrorLog errorLog) {
        logClient.saveErrorLog(errorLog);
    }

    /**
     * 保存操作审计日志
     *
     * @param usualLog 操作日志信息
     */
    @Override
    public void saveAuditUsualLog(AuditUsualLog usualLog) {
        logClient.saveUsualLog(usualLog);
    }
}
