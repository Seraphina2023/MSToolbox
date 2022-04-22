package com.msop.core.log.service.impl;

import cn.hutool.core.util.StrUtil;
import com.msop.core.log.model.AuditApiLog;
import com.msop.core.log.model.AuditErrorLog;
import com.msop.core.log.model.AuditUsualLog;
import com.msop.core.log.properties.LogDbProperties;
import com.msop.core.log.service.IAuditService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * 审计日志实现类-数据库
 *
 * @author ruozhuliufeng
 */
@Slf4j
@Service
@ConditionalOnClass(JdbcTemplate.class)
@ConditionalOnProperty(name = "ms.audit-log.log-type", havingValue = "db")
public class DbAuditServiceImpl implements IAuditService {
    /**
     * 数据库插入语句
     */
    private static final String INSERT_API_URL = "INSERT INTO audit_api_log " +
            " (tenant_id,trace_id,service_id,server_host,server_ip,type,title," +
            "method,request_uri,user_agent,remote_ip,method_class,method_name,params,time,create_by,create_time) " +
            " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_ERROR_URL = "INSERT INTO audit_error_log " +
            " (tenant_id,service_id,server_host,server_ip,trace_id,method,request_uri,user_agent," +
            "stack_trace,exception_name,message,line_number,remote_ip,method_class,file_name,method_name," +
            "params,create_by,create_time) " +
            " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static final String INSERT_USUAL_URL = "INSERT INTO audit_usual_log " +
            " (tenant_id,service_id,server_host,server_ip,log_level,log_id,log_data,method," +
            "request_uri,remote_ip,method_class,method_name,user_agent,params,create_by,create_time) " +
            " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private final JdbcTemplate jdbcTemplate;

    public DbAuditServiceImpl(@Autowired(required = false) LogDbProperties logDbProperties, DataSource dataSource) {
        // 优先使用配置的日志数据源，否则使用默认的数据源
        if (logDbProperties != null && StrUtil.isNotEmpty(logDbProperties.getJdbcUrl())) {
            dataSource = new HikariDataSource(logDbProperties);
        }
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 初始化数据库
     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，
     * 并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。
     */
    @PostConstruct
    public void init() {
        String createAuditApiLogSql = "CREATE TABLE IF NOT EXISTS `audit_api_log` (\n" +
                "  `tenant_id` varchar(12) DEFAULT '000000' COMMENT '租户ID',\n" +
                "  `trace_id` varchar(12) DEFAULT NULL COMMENT '链路ID',\n" +
                "  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',\n" +
                "  `server_host` varchar(255) DEFAULT NULL COMMENT '服务器名',\n" +
                "  `server_ip` varchar(255) DEFAULT NULL COMMENT '服务器IP地址',\n" +
                "  `type` char(1) DEFAULT '1' COMMENT '日志类型',\n" +
                "  `title` varchar(255) DEFAULT '' COMMENT '日志标题',\n" +
                "  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',\n" +
                "  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',\n" +
                "  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',\n" +
                "  `remote_ip` varchar(255) DEFAULT NULL COMMENT '操作IP地址',\n" +
                "  `method_class` varchar(255) DEFAULT NULL COMMENT '方法类',\n" +
                "  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名',\n" +
                "  `params` text COMMENT '操作提交的数据',\n" +
                "  `time` varchar(64) DEFAULT NULL COMMENT '执行时间',\n" +
                "  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',\n" +
                "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口日志表';";
        String createAuditErrorLogSql = "CREATE TABLE IF NOT EXISTS  `audit_error_log` (\n" +
                "  `tenant_id` varchar(12) DEFAULT '000000' COMMENT '租户ID',\n" +
                "  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',\n" +
                "  `server_host` varchar(255) DEFAULT NULL COMMENT '服务器名',\n" +
                "  `server_ip` varchar(255) DEFAULT NULL COMMENT '服务器IP地址',\n" +
                "  `trace_id` varchar(255) DEFAULT NULL COMMENT '链路ID',\n" +
                "  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',\n" +
                "  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',\n" +
                "  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',\n" +
                "  `stack_trace` text COMMENT '堆栈',\n" +
                "  `exception_name` varchar(255) DEFAULT NULL COMMENT '异常名',\n" +
                "  `message` text COMMENT '异常信息',\n" +
                "  `line_number` int(11) DEFAULT NULL COMMENT '错误行数',\n" +
                "  `remote_ip` varchar(255) DEFAULT NULL COMMENT '操作IP地址',\n" +
                "  `method_class` varchar(255) DEFAULT NULL COMMENT '方法类',\n" +
                "  `file_name` varchar(1000) DEFAULT NULL COMMENT '文件名',\n" +
                "  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名',\n" +
                "  `params` text COMMENT '操作提交的数据',\n" +
                "  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',\n" +
                "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='错误日志表';";
        String createAuditUsualLogSql = "CREATE TABLE IF NOT EXISTS `audit_usual_log` (\n" +
                "  `tenant_id` varchar(12) DEFAULT '000000' COMMENT '租户ID',\n" +
                "  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',\n" +
                "  `server_host` varchar(255) DEFAULT NULL COMMENT '服务器名',\n" +
                "  `server_ip` varchar(255) DEFAULT NULL COMMENT '服务器IP地址',\n" +
                "  `trace_id` varchar(255) DEFAULT NULL COMMENT '链路ID',\n" +
                "  `log_level` varchar(10) DEFAULT NULL COMMENT '日志级别',\n" +
                "  `log_id` varchar(100) DEFAULT NULL COMMENT '日志业务id',\n" +
                "  `log_data` text COMMENT '日志数据',\n" +
                "  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',\n" +
                "  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',\n" +
                "  `remote_ip` varchar(255) DEFAULT NULL COMMENT '操作IP地址',\n" +
                "  `method_class` varchar(255) DEFAULT NULL COMMENT '方法类',\n" +
                "  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名',\n" +
                "  `user_agent` varchar(1000) DEFAULT NULL COMMENT '用户代理',\n" +
                "  `params` text COMMENT '操作提交的数据',\n" +
                "  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',\n" +
                "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' \n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通用日志表';";
        this.jdbcTemplate.execute(createAuditApiLogSql);
        this.jdbcTemplate.execute(createAuditUsualLogSql);
        this.jdbcTemplate.execute(createAuditErrorLogSql);
    }


    /**
     * 保存接口审计日志
     *
     * @param apiLog 接口日志信息
     */
    @Async
    @Override
    public void saveAuditApiLog(AuditApiLog apiLog) {
        this.jdbcTemplate.update(INSERT_API_URL, apiLog.getTenantId(), apiLog.getTraceId(), apiLog.getServiceId(),
                apiLog.getServerHost(), apiLog.getServerIp(), apiLog.getType(), apiLog.getTitle(), apiLog.getMethod(),
                apiLog.getRequestUri(), apiLog.getUserAgent(), apiLog.getRemoteIp(), apiLog.getMethodClass(), apiLog.getMethodName(),
                apiLog.getParams(), apiLog.getTime(), apiLog.getCreateUser(), apiLog.getCreateTime());
    }

    /**
     * 保存错误审计日志
     *
     * @param errorLog 错误日志信息
     */
    @Async
    @Override
    public void saveAuditErrorLog(AuditErrorLog errorLog) {
        this.jdbcTemplate.update(INSERT_ERROR_URL, errorLog.getTenantId(), errorLog.getServiceId(), errorLog.getServerHost(),
                errorLog.getServerIp(), errorLog.getTraceId(), errorLog.getMethod(), errorLog.getRequestUri(), errorLog.getUserAgent(),
                errorLog.getStackTrace(), errorLog.getExceptionName(), errorLog.getMessage(), errorLog.getLineNumber(), errorLog.getRemoteIp(),
                errorLog.getMethodClass(), errorLog.getFileName(), errorLog.getMethodName(), errorLog.getParams(), errorLog.getCreateUser(), errorLog.getCreateTime());
    }

    /**
     * 保存操作审计日志
     *
     * @param usualLog 操作日志信息
     */
    @Async
    @Override
    public void saveAuditUsualLog(AuditUsualLog usualLog) {
        this.jdbcTemplate.update(INSERT_USUAL_URL, usualLog.getTenantId(), usualLog.getServiceId(), usualLog.getServerHost(),
                usualLog.getServerIp(), usualLog.getTraceId(), usualLog.getLogLevel(), usualLog.getLogId(), usualLog.getLogData(),
                usualLog.getMethod(), usualLog.getMethod(), usualLog.getRequestUri(), usualLog.getRemoteIp(), usualLog.getMethodClass(),
                usualLog.getMethodName(), usualLog.getUserAgent(), usualLog.getParams(), usualLog.getCreateUser(), usualLog.getCreateTime());
    }
}
