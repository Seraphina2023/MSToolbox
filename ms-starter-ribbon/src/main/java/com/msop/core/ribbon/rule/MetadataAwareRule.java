package com.msop.core.ribbon.rule;

import com.msop.core.ribbon.predicate.DiscoveryEnabledPredicate;
import com.msop.core.ribbon.predicate.MetadataAwarePredicate;
import com.msop.core.ribbon.support.MsRibbonRuleProperties;
import com.msop.core.ribbon.utils.BeanUtil;
import com.msop.core.ribbon.utils.HostUtil;
import com.netflix.loadbalancer.Server;
import org.springframework.util.ObjectUtils;
import org.springframework.util.PatternMatchUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ribbon 路由规则器
 *
 * @author ruozhuliufeng
 */
public class MetadataAwareRule extends DiscoveryEnabledRule {


    public MetadataAwareRule() {
        super(MetadataAwarePredicate.INSTANCE);
    }

    /**
     * 过滤服务
     *
     * @param serverList 服务列表
     * @return 服务列表
     */
    @Override
    public List<Server> filterServers(List<Server> serverList) {
        // 获取配置
        MsRibbonRuleProperties properties = BeanUtil.getBean(MsRibbonRuleProperties.class);
        List<String> priorIpPattern = properties.getPriorIpPattern();
        // 查找是否有本机IP
        String hostIP = HostUtil.getHostIp();
        // 优先的IP规则
        boolean hasPriorIpPattern = !priorIpPattern.isEmpty();
        String[] priorIpPatterns = priorIpPattern.toArray(new String[0]);
        List<Server> priorServerList = new ArrayList<>();
        for (Server server : serverList) {
            String host = server.getHost();
            // 优先本地IP服务
            if (ObjectUtils.nullSafeEquals(hostIP, host)) {
                return Collections.singletonList(server);
            }
            // 优先的ip服务
            if (hasPriorIpPattern && PatternMatchUtils.simpleMatch(priorIpPatterns, host)) {
                priorServerList.add(server);
            }
        }
        if (!priorServerList.isEmpty()) {
            return priorServerList;
        }
        return Collections.unmodifiableList(serverList);
    }
}
