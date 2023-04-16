package tech.msop.core.db.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DB 数据库配置
 */
@Data
@ConfigurationProperties("ms.db")
public class MsDbProperties {
    /**
     * 是否启用生成表 默认否
     */
    Boolean enabled = Boolean.FALSE;
    /**
     * 数据库前缀
     */
    String tablePrefix = "";
    /**
     * 是否允许驼峰转换 将类名转换为驼峰格式的数据库表名
     */
    Boolean camelCase = Boolean.TRUE;
    /**
     * 实体类所在路径
     */
    String[] packages;
}
