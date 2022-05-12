package com.msop.core.tool.config;

import com.msop.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 工具配置类
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ToolConfiguration {

    /**
     * Spring 上下文缓存
     * @return SpringUtil
     */
    @Bean
    public SpringUtil springUtil(){
        return new SpringUtil();
    }
}
