package com.msop.core.log.config;

import com.msop.core.launch.properties.MsPropertySource;
import com.msop.core.log.aspect.AuditLogAspect;
import com.msop.core.log.aspect.LogTraceAspect;
import com.msop.core.log.event.AuditApiLogListener;
import com.msop.core.log.event.AuditErrorLogListener;
import com.msop.core.log.event.AuditUsualLogListener;
import com.msop.core.log.filter.LogTraceFilter;
import com.msop.core.log.logger.MsLogger;
import com.msop.core.log.properties.AuditLogProperties;
import com.msop.core.log.properties.LogDbProperties;
import com.msop.core.log.service.IAuditService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.annotation.Resource;
import javax.servlet.DispatcherType;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication
@EnableConfigurationProperties({AuditLogProperties.class, LogDbProperties.class})
@MsPropertySource(value = "classpath:/ms-log.yml")
public class MsLogToolAutoConfiguration {

    @Bean
    public AuditLogAspect auditLogAspect(){
        return new AuditLogAspect();
    }

    @Bean
    public LogTraceAspect logTraceAspect(){
        return new LogTraceAspect();
    }

    @Bean
    public MsLogger msLogger(){
        return new MsLogger();
    }

    @Bean
    public FilterRegistrationBean<LogTraceFilter> logTraceFilterRegistration(){
        FilterRegistrationBean<LogTraceFilter> registration = new FilterRegistrationBean<>();
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.setFilter(new LogTraceFilter());
        registration.addUrlPatterns("/*");
        registration.setName("LogTraceFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }

    @Bean
    public AuditApiLogListener auditApiLogListener(){
        return new AuditApiLogListener();
    }
    @Bean
    public AuditErrorLogListener auditErrorLogListener(){
        return new AuditErrorLogListener();
    }
    @Bean
    public AuditUsualLogListener auditUsualLogListener(){
        return new AuditUsualLogListener();
    }
}
