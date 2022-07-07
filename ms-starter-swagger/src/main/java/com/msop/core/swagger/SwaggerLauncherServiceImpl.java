package com.msop.core.swagger;

import com.msop.core.auto.service.AutoService;
import com.msop.core.launch.constant.AppConstant;
import com.msop.core.launch.service.LauncherService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

import java.util.Properties;

/**
 * 初始化Swagger配置
 *
 * @author ruozhuliufeng
 */
@AutoService(LauncherService.class)
public class SwaggerLauncherServiceImpl implements LauncherService {
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
        Properties properties = System.getProperties();
        if (profile.equals(AppConstant.PROD_MODE)) {
            properties.setProperty("knife4j.production", "true");
        }
        properties.setProperty("knife4j.enable", "true");
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
