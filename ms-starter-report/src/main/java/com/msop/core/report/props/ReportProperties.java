package com.msop.core.report.props;

import com.msop.core.tool.constant.StringConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * UReport 配置类
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties(prefix = "report")
public class ReportProperties {
    /**
     * 是否启用
     */
    private Boolean enabled = true;
    /**
     * 禁用缓存
     */
    private Boolean disableHttpSessionReportCache = false;
    /**
     * 禁用文件provider
     */
    private Boolean disableFileProvider = true;
    /**
     * 文件存储路径
     */
    private String fileStoreDir = StringConstant.EMPTY;
    /**
     * debug
     */
    private Boolean debug = false;
}
