package tech.msop.core.loadbalancer.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * LoadBalancer 配置
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties(MsLoadBalancerProperties.PROPERTIES_PREFIX)
public class MsLoadBalancerProperties {
    public static final String PROPERTIES_PREFIX = "ms.loadbalancer";
    /**
     * 是否开启自定义负载均衡
     */
    private boolean enabled = true;
    /**
     * 灰度服务版本
     */
    private String version;
    /**
     * 优先的ip列表，支持通配符，例如：10.20.0.8*,10.20.0.*
     */
    private List<String> priorIpPattern = new ArrayList<>();
}
