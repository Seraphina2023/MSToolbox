package com.msop.core.boot.config;

import com.msop.core.launch.properties.MsPropertySource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * MS 自动配置类
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)
@MsPropertySource(value="classpath:/ms-boot.yml")
public class MsBootAutoConfiguration {
}
