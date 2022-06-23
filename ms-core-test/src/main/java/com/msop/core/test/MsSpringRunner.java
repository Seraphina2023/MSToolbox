package com.msop.core.test;

import com.msop.core.launch.MsApplication;
import com.msop.core.launch.constant.AppConstant;
import com.msop.core.launch.constant.NacosConstant;
import com.msop.core.launch.constant.SentinelConstant;
import com.msop.core.launch.service.LauncherService;
import org.junit.runners.model.InitializationError;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设置启动参数
 */
public class MsSpringRunner extends SpringJUnit4ClassRunner {
    public MsSpringRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        setUpTestClass(clazz);
    }

    private void setUpTestClass(Class<?> clazz) {
        MsBootTest msBootTest = AnnotationUtils.getAnnotation(clazz,MsBootTest.class);
        if (msBootTest == null){
            throw new MSBootTestException("类名必须有@MsBootTest 注解");
        }
        String appName = msBootTest.appName();
        String profile = msBootTest.profile();
        boolean isLocalDev = MsApplication.isLocalDev();
        Properties props = System.getProperties();
        props.setProperty("ms.env", profile);
        props.setProperty("ms.name", appName);
        props.setProperty("ms.is-local", String.valueOf(isLocalDev));
        props.setProperty("ms.dev-mode", profile.equals(AppConstant.PROD_MODE) ? "false" : "true");
        props.setProperty("ms.service.version", AppConstant.APPLICATION_VERSION);
        props.setProperty("spring.application.name", appName);
        props.setProperty("spring.profiles.active", profile);
        props.setProperty("info.version", AppConstant.APPLICATION_VERSION);
        props.setProperty("info.desc", appName);
        props.setProperty("spring.cloud.sentinel.transport.dashboard", SentinelConstant.SENTINEL_ADDR);
        props.setProperty("spring.main.allow-bean-definition-overriding", "true");
        props.setProperty("spring.cloud.nacos.config.shared-configs[0].data-id", NacosConstant.sharedDataId());
        props.setProperty("spring.cloud.nacos.config.shared-configs[0].group", NacosConstant.NACOS_CONFIG_GROUP);
        props.setProperty("spring.cloud.nacos.config.shared-configs[0].refresh", NacosConstant.NACOS_CONFIG_REFRESH);
        props.setProperty("spring.cloud.nacos.config.file-extension", NacosConstant.NACOS_CONFIG_FORMAT);
        props.setProperty("spring.cloud.nacos.config.shared-configs[1].data-id", NacosConstant.sharedDataId(profile));
        props.setProperty("spring.cloud.nacos.config.shared-configs[1].group", NacosConstant.NACOS_CONFIG_GROUP);
        props.setProperty("spring.cloud.nacos.config.shared-configs[1].refresh", NacosConstant.NACOS_CONFIG_REFRESH);
        // 加载自定义组件
        if (msBootTest.enableLoader()) {
            SpringApplicationBuilder builder = new SpringApplicationBuilder(clazz);
            List<LauncherService> launcherList = new ArrayList<>();
            ServiceLoader.load(LauncherService.class).forEach(launcherList::add);
            launcherList.stream().sorted(Comparator.comparing(LauncherService::getOrder)).collect(Collectors.toList())
                    .forEach(launcherService -> launcherService.launcher(builder, appName, profile, isLocalDev));
        }
        System.err.println(String.format("---[junit.test]:[%s]---启动中，读取到的环境变量:[%s]", appName, profile));
    }
}
