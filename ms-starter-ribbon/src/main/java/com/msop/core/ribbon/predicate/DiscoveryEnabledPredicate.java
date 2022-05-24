package com.msop.core.ribbon.predicate;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * 过滤服务
 *
 * @author ruozhuliufeng
 */
public abstract class DiscoveryEnabledPredicate extends AbstractServerPredicate {
    @Override
    public boolean apply(@Nullable PredicateKey predicateKey) {
        return predicateKey!=null
                && predicateKey.getServer() instanceof NacosServer
                && apply((NacosServer) predicateKey.getServer());
    }

    /**
     * Returns whether the specific {@link NacosServer} matches this predicate.
     *
     * @param server the discovered server
     * @return whether the server matches the predicate
     */
    protected abstract boolean apply(NacosServer server);
}
