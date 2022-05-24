package com.msop.core.ribbon.predicate;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.msop.core.ribbon.support.MsRibbonRuleProperties;
import com.msop.core.ribbon.utils.BeanUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 基于Metadata 的服务筛选
 *
 * @author ruozhuliufeng
 */
public class MetadataAwarePredicate extends DiscoveryEnabledPredicate {

    public static final MetadataAwarePredicate INSTANCE = new MetadataAwarePredicate();

    /**
     * Returns whether the specific {@link NacosServer} matches this predicate.
     *
     * @param server the discovered server
     * @return whether the server matches the predicate
     */
    @Override
    protected boolean apply(NacosServer server) {
        final Map<String, String> metadata = server.getMetadata();

        // 获取配置
        MsRibbonRuleProperties properties = BeanUtil.getBean(MsRibbonRuleProperties.class);
        // 服务里的配置
        String localTag = properties.getTag();
        if (StringUtils.isBlank(localTag)) {
            return true;
        }
        // 本地有tag,服务没有，返回false
        String metaDataTag = metadata.get("tag");
        if (StringUtils.isBlank(metaDataTag)) {
            return false;
        }
        return metaDataTag.equals(localTag);
    }
}
