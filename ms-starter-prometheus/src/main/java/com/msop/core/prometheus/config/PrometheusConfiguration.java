package com.msop.core.prometheus.config;


import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.msop.core.launch.properties.MsPropertySource;
import com.msop.core.prometheus.endpoint.AgentEndpoint;
import com.msop.core.prometheus.endpoint.ServiceEndpoint;
import com.msop.core.prometheus.service.RegistrationService;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@MsPropertySource("classpath:/ms-prometheus.yml")
public class PrometheusConfiguration {

    @Bean
    public RegistrationService registrationService(DiscoveryClient discoveryClient){
        return new RegistrationService(discoveryClient);
    }

    @Bean
    public AgentEndpoint agentController(NacosDiscoveryProperties properties){
        return new AgentEndpoint(properties);
    }

    @Bean
    public ServiceEndpoint serviceController(RegistrationService registrationService){
        return new ServiceEndpoint(registrationService);
    }
}
