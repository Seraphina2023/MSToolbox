package com.msop.core.tracce;

import com.msop.core.launch.properties.MsPropertySource;
import org.springframework.context.annotation.Configuration;

/**
 * TraceAutoConfiguration
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@MsPropertySource(value = "classpath:/ms-trace.yml")
public class TraceAutoConfiguration {
}
