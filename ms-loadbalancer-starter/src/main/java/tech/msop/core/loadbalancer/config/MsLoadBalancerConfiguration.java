package tech.msop.core.loadbalancer.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientConfiguration;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import tech.msop.core.loadbalancer.props.MsLoadBalancerProperties;
import tech.msop.core.loadbalancer.rule.GrayscaleLoadBalancer;

@AutoConfiguration(before = LoadBalancerClientConfiguration.class)
@EnableConfigurationProperties(MsLoadBalancerProperties.class)
@ConditionalOnProperty(value = MsLoadBalancerProperties.PROPERTIES_PREFIX + ".enabled",matchIfMissing = true)
@Order(MsLoadBalancerConfiguration.REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER)
public class MsLoadBalancerConfiguration {
    public static final int REACTIVE_SERVICE_INSTANCE_SUPPLIER_ORDER = 191231234;

    @Bean
    public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(Environment environment,
                                                                                   LoadBalancerClientFactory loadBalancerClientFactory,
                                                                                   MsLoadBalancerProperties msLoadBalancerProperties){
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new GrayscaleLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),msLoadBalancerProperties);
    }

    @Bean
    public LoadBalancerClientSpecification loadBalancerClientSpecification(){
        return new LoadBalancerClientSpecification("default.msLoadBalancerConfiguration",
                new Class[]{MsLoadBalancerConfiguration.class});
    }
}
