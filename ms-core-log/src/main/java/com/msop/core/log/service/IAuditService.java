package com.msop.core.log.service;


import com.msop.core.log.model.AuditApiLog;
import com.msop.core.log.model.AuditErrorLog;
import com.msop.core.log.model.AuditUsualLog;

/**
 * 审计日志接口
 *
 * @author ruozhuliufeng
 * @date 2021-07-17
 */
public interface IAuditService {
    /**
     * 保存接口审计日志
     *
     * @param apiLog 接口日志信息
     */
    void saveAuditApiLog(AuditApiLog apiLog);

    /**
     * 保存错误审计日志
     *
     * @param errorLog 错误日志信息
     */
    void saveAuditErrorLog(AuditErrorLog errorLog);

    /**
     * 保存操作审计日志
     *
     * @param usualLog 操作日志信息
     */
    void saveAuditUsualLog(AuditUsualLog usualLog);
}
