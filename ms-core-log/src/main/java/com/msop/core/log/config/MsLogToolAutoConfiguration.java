package com.msop.core.log.config;

import com.msop.core.log.aspect.AuditLogAspect;
import com.msop.core.log.event.AuditApiLogListener;
import com.msop.core.log.event.AuditErrorLogListener;
import com.msop.core.log.event.AuditUsualLogListener;
import com.msop.core.log.logger.MsLogger;
import com.msop.core.log.service.IAuditService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnWebApplication
public class MsLogToolAutoConfiguration {
    private final IAuditService auditService;
    @Value("${spring.application.name}")
    private String serviceId;
    @Bean
    public AuditLogAspect auditLogAspect(){
        return new AuditLogAspect();
    }

    @Bean
    public MsLogger msLogger(){
        return new MsLogger();
    }

    @Bean
    public AuditApiLogListener auditApiLogListener(){
        return new AuditApiLogListener(auditService,serviceId);
    }
    @Bean
    public AuditErrorLogListener auditErrorLogListener(){
        return new AuditErrorLogListener(auditService,serviceId);
    }
    @Bean
    public AuditUsualLogListener auditUsualLogListener(){
        return new AuditUsualLogListener(auditService,serviceId);
    }
}
