package com.msop.core.report.config;

import com.bstek.ureport.UReportPropertyPlaceholderConfigurer;
import com.bstek.ureport.console.UReportServlet;
import com.bstek.ureport.provider.report.ReportProvider;
import com.msop.core.report.props.ReportDatabaseProperties;
import com.msop.core.report.props.ReportProperties;
import com.msop.core.report.provider.DatabaseProvider;
import com.msop.core.report.provider.ReportPlaceholderProvider;
import com.msop.core.report.service.IReportFileService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;

import javax.servlet.Servlet;

/**
 * UReport 配置类
 *
 * @author ruozhuliufeng
 */
@Order
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "report.enabled",havingValue = "true",matchIfMissing = true)
@EnableConfigurationProperties({ReportProperties.class, ReportDatabaseProperties.class})
@ImportResource("classpath:ureport-console-context.xml")
public class ReportConfiguration {

    @Bean
    public ServletRegistrationBean<Servlet> registrationBean(){
        return new ServletRegistrationBean<>(new UReportServlet(), "/report/**");
    }

    @Bean
    public UReportPropertyPlaceholderConfigurer uReportPropertyPlaceholderConfigurer(ReportProperties properties){
        return new ReportPlaceholderProvider(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReportProvider reportProvider(ReportDatabaseProperties properties, IReportFileService service){
        return new DatabaseProvider(properties, service);
    }
}
