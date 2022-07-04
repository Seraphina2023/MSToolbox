package com.msop.core.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.msop.core.mp.config.MybatisPlusConfiguration;
import com.msop.core.tenant.MsTenantHandler;
import com.msop.core.tenant.MsTenantId;
import com.msop.core.tenant.MsTenantInterceptor;
import com.msop.core.tenant.TenantId;
import com.msop.core.tenant.aspect.MsTenantAspect;
import com.msop.core.tenant.properties.MsTenantProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 多租户配置类
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@AutoConfigureBefore(MybatisPlusConfiguration.class)
@EnableConfigurationProperties(MsTenantProperties.class)
public class TenantConfiguration {

    /**
     * 自定义多租户处理器
     *
     * @param tenantProperties 多租户配置类
     * @return TenantHandler
     */
    @Bean
    @Primary
    public TenantLineHandler msTenantLineHandler(MsTenantProperties tenantProperties) {
        return new MsTenantHandler(tenantProperties);
    }

    /**
     * 自定义租户拦截器
     *
     * @param tenantHandler    多租户处理器
     * @param tenantProperties 多租户配置类
     * @return MsTenantInterceptor
     */
    @Bean
    @Primary
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantLineHandler tenantHandler, MsTenantProperties tenantProperties) {
        MsTenantInterceptor tenantInterceptor = new MsTenantInterceptor();
        tenantInterceptor.setTenantLineHandler(tenantHandler);
        tenantInterceptor.setTenantProperties(tenantProperties);
        return tenantInterceptor;
    }


    /**
     * 自定义租户ID生成器
     *
     * @return TenantId
     */
    @Bean
    @ConditionalOnMissingBean(TenantId.class)
    public TenantId tenantId() {
        return new MsTenantId();
    }

    /**
     * 自定义租户界面
     *
     * @return
     */
    @Bean
    public MsTenantAspect msTenantAspect() {
        return new MsTenantAspect();
    }
}
