package com.msop.core.log.launch;

import com.msop.core.auto.service.AutoService;
import com.msop.core.launch.service.LauncherService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

import java.util.Properties;

/**
 * 日志启动配置类
 *
 * @author ruozhuliufeng
 */
@AutoService(LauncherService.class)
public class LogLauncherServiceImpl implements LauncherService {
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
        properties.setProperty("logging.config", "classpath:log/logback-" + profile + ".xml");
    }

    /**
     * 获取排列顺序
     *
     * @return order
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
