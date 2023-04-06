package tech.msop.core.log.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import tech.msop.core.launch.properties.MsProperties;
import tech.msop.core.launch.properties.MsPropertySource;
import tech.msop.core.launch.server.ServerInfo;
import tech.msop.core.log.aspect.AuditLogAspect;
import tech.msop.core.log.aspect.LogTraceAspect;
import tech.msop.core.log.event.AuditApiLogListener;
import tech.msop.core.log.event.AuditErrorLogListener;
import tech.msop.core.log.event.AuditUsualLogListener;
import tech.msop.core.log.feign.ILogClient;
import tech.msop.core.log.filter.LogTraceFilter;
import tech.msop.core.log.logger.MsLogger;
import tech.msop.core.log.properties.AuditLogProperties;
import tech.msop.core.log.properties.LogDbProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import tech.msop.core.log.service.IAuditService;

import javax.servlet.DispatcherType;

@AutoConfiguration
@ConditionalOnWebApplication
@EnableConfigurationProperties({AuditLogProperties.class,LogDbProperties.class})
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
    @ConditionalOnMissingBean(name = "apiLogListener")
    public AuditApiLogListener auditApiLogListener(IAuditService auditService, ServerInfo serverInfo, MsProperties properties){
        return new AuditApiLogListener(auditService,serverInfo,properties);
    }
    @Bean
    @ConditionalOnMissingBean(name = "errorLogListener")
    public AuditErrorLogListener auditErrorLogListener(IAuditService auditService, ServerInfo serverInfo, MsProperties properties){
        return new AuditErrorLogListener(auditService,serverInfo,properties);
    }
    @Bean
    @ConditionalOnMissingBean(name = "usualLogListener")
    public AuditUsualLogListener auditUsualLogListener(IAuditService auditService, ServerInfo serverInfo, MsProperties properties){
        return new AuditUsualLogListener(auditService,serverInfo,properties);
    }
}
