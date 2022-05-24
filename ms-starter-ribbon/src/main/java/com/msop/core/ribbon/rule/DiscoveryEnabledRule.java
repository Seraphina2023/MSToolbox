package com.msop.core.ribbon.rule;

import com.msop.core.ribbon.predicate.DiscoveryEnabledPredicate;
import com.netflix.loadbalancer.*;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Ribbon 路由规则
 *
 * @author ruozhuliufeng
 */
public abstract class DiscoveryEnabledRule extends PredicateBasedRule {

    private final CompositePredicate predicate;

    public DiscoveryEnabledRule(DiscoveryEnabledPredicate discoveryEnabledPredicate) {
        Assert.notNull(discoveryEnabledPredicate, "Parameter 'discoveryEnabledPredicate' can not be null");
        this.predicate = createCompositePredicate(discoveryEnabledPredicate, new AvailabilityPredicate(this, null));
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return predicate;
    }

    @Override
    public Server choose(Object key) {
        ILoadBalancer lb = getLoadBalancer();
        List<Server> allServers = lb.getAllServers();
        // 过滤服务列表
        List<Server> serverList = filterServers(allServers);
        return getPredicate().chooseRandomlyAfterFiltering(serverList,key).orNull();
    }

    /**
     * 过滤服务
     *
     * @param serverList 服务列表
     * @return 服务列表
     */
    public abstract List<Server> filterServers(List<Server> serverList);

    private CompositePredicate createCompositePredicate(DiscoveryEnabledPredicate discoveryEnabledPredicate, AvailabilityPredicate availabilityPredicate) {
        CompositePredicate.Builder builder = CompositePredicate.withPredicates(discoveryEnabledPredicate, availabilityPredicate);
        return builder.build();
    }
}
