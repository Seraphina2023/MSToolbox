package tech.msop.core.log.properties;

import tech.msop.core.launch.log.MsLogLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 审计日志配置
 * @RefreshScope 实现配置、类热部署
 * @author ruozhuliufeng
 */
@Setter
@Getter
@ConfigurationProperties(prefix = MsLogLevel.LOG_PROPS_PREFIX)
public class AuditLogProperties {
    /**
     * 是否开启审计日志
     */
    private Boolean enabled = true;
    /**
     * 是否开启异常日志推送
     */
    private Boolean errorLog = true;
    /**
     * 是否开启控制台日志输出
     */
    private Boolean console = true;
    /**
     * 日志记录类型(logger/redis/db/es/feign)
     */
    private String logType = "logger";
    /**
     * 日志记录级别
     */
    private MsLogLevel level = MsLogLevel.BODY;
}
