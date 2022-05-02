package com.msop.core.mybatis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * Mybatis-Plus 自动注入配置
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "ms.mybatis-plus.auto-fill")
@RefreshScope
public class MybatisPlusAutoFillProperties {

    /**
     * 是否开启自动填充字段
     */
    private Boolean enabled = true;
    /**
     * 是否开启插入填充
     */
    private Boolean enableInsertFill = true;
    /**
     * 是否开启更新填充
     */
    private Boolean enableUpdateFill = true;
    /**
     * 创建时间字段名
     */
    private String createTimeField = "createTime";
    /**
     * 更新时间字段名
     */
    private String updateTimeField = "updateTime";

    /**
     * 创建用户字段名
     */
    private String createUserField = "createUser";

    /**
     * 更新用户字段名
     */
    private String updateUserField = "updateUser";
}
