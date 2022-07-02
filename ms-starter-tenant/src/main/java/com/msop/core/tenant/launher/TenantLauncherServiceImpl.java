package com.msop.core.tenant.launher;

import com.msop.core.auto.service.AutoService;
import com.msop.core.launch.service.LauncherService;
import com.msop.core.launch.utils.PropsUtil;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

import java.util.Properties;

/**
 * 初始化租户配置
 *
 * @author ruozhuliufeng
 */
@AutoService(LauncherService.class)
public class TenantLauncherServiceImpl implements LauncherService {

    /**
     * 启动时，处理 SpringApplicaionBuilder
     *
     * @param builder    SpringApplicationBuilder
     * @param appName    SpringApplicationAppName
     * @param profile    SpringApplicationProfile
     * @param isLocalDev SpringApplicationIsLocalDev
     */
    @Override
    public void launcher(SpringApplicationBuilder builder, String appName, String profile, boolean isLocalDev) {
        Properties props = System.getProperties();
        //默认关闭mybatis-plus多数据源自动装配
        PropsUtil.setProperty(props, "spring.datasource.dynamic.enabled", "false");
    }

    /**
     * 获取排列顺序
     *
     * @return order
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
