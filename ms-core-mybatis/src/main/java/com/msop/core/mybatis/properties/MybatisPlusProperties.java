package com.msop.core.mybatis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mybatis Plus 配置类
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties("ms.mybatis-plus")
public class MybatisPlusProperties {
    /**
     * 分页最大数
     */
    private Long pageLimit = 500L;
    /**
     * 溢出总页数后是否进行处理
     */
    protected Boolean overflow = false;
}
