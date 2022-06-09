package com.msop.core.promethus.endpoint;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.msop.core.promethus.data.Agent;
import com.msop.core.promethus.data.Config;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Agent 端点
 *
 * @author ruozhuliufeng
 */
@RestController
public class AgentEndpoint {
    private final NacosDiscoveryProperties properties;

    public AgentEndpoint(final NacosDiscoveryProperties properties) {
        this.properties = properties;
    }

    @GetMapping(value = "/v1/agent/self", produces = {"application/json"})
    public Agent getNodes() {
        Config config = Config.builder().dataCenter(this.properties.getGroup()).build();
        return Agent.builder().config(config).build();
    }
}
