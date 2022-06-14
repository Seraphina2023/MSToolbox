package com.msop.core.transaction.annotation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.lang.annotation.*;

/**
 * Seata 启动注解配置
 *
 * @author ruozhuliufeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class
})
public @interface SeataCloudApplication {
}
