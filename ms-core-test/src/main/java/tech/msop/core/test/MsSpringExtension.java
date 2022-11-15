package tech.msop.core.test;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.lang.NonNull;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tech.msop.core.launch.MsApplication;
import tech.msop.core.launch.constant.AppConstant;
import tech.msop.core.launch.constant.NacosConstant;
import tech.msop.core.launch.constant.SentinelConstant;
import tech.msop.core.launch.service.LauncherService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 设置启动参数
 */
public class MsSpringExtension extends SpringExtension {
    @Override
    public void beforeAll(@NonNull ExtensionContext context) throws Exception {
        super.beforeAll(context);
        setUpTestClass(context);
    }

    private void setUpTestClass(ExtensionContext context) {
        Class<?> clazz = context.getRequiredTestClass();
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
        props.setProperty("loadbalancer.client.name",appName);
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
        System.err.printf("---[junit.test]:[%s]---启动中，读取到的环境变量:[%s]%n", appName, profile);
    }
}
