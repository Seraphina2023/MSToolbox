package com.msop.core.report.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * UReport 配置类
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties(prefix = "report.database.provider")
public class ReportDatabaseProperties {
    private String name = "数据库文件系统";

    private String prefix = "msop-";

    private boolean disabled = false;
}
