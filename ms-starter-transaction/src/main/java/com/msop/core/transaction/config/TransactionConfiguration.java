package com.msop.core.transaction.config;

import com.msop.core.launch.properties.MsPropertySource;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Seata 配置类
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@MsPropertySource("classpath:/ms-transaction.yml")
public class TransactionConfiguration {
}
