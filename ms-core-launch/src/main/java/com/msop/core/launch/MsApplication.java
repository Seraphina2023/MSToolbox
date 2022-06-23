package com.msop.core.launch;


import com.msop.core.launch.constant.AppConstant;
import com.msop.core.launch.constant.NacosConstant;
import com.msop.core.launch.service.LauncherService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cglib.core.internal.Function;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.*;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目启动器，解决环境变量问题
 *
 * @author ruozhuliufeng
 */
public class MsApplication {

    /**
     * 创建一个应用程序的上下文
     * java -jar app.jar --spring.profiles.active=prod --server.port=2333
     *
     * @param appName 应用名称
     * @param source  source
     * @param args    参数
     * @return an application context created from the current state
     */
    public static ConfigurableApplicationContext run(String appName, Class source, String... args) {
        SpringApplicationBuilder builder = createSpringApplicationBuilder(appName, source, args);
        return builder.run(args);
    }

    public static SpringApplicationBuilder createSpringApplicationBuilder(String appName, Class source, String[] args) {
        Assert.hasText(appName, "[appName]服务名不能为空");
        // 读取环境变量，使用 spring boot的规则
        ConfigurableEnvironment environment = new StandardEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new SimpleCommandLinePropertySource(args));
        propertySources.addLast(new MapPropertySource(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, environment.getSystemProperties()));
        propertySources.addLast(new SystemEnvironmentPropertySource(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, environment.getSystemProperties()));
        // 获取配置的环境变量
        String[] activeProfiles = environment.getActiveProfiles();
        // 判断环境：dev test prod
        List<String> profiles = Arrays.asList(activeProfiles);
        // 预设的环境
        List<String> presetProfiles = new ArrayList<>(Arrays.asList(AppConstant.DEV_MODE, AppConstant.PROD_MODE, AppConstant.TEST_MODE));
        // 交集
        presetProfiles.retainAll(profiles);
        // 当前使用
        List<String> activeProfileList = new ArrayList<>(profiles);
        Function<Object[], String> joinFun = StringUtils::arrayToCommaDelimitedString;
        SpringApplicationBuilder builder = new SpringApplicationBuilder(source);
        String profile;
        if (activeProfileList.isEmpty()) {
            // 默认DEV开发
            profile = AppConstant.DEV_MODE;
            activeProfileList.add(profile);
            builder.profiles(profile);
        } else if (activeProfileList.size() == 1) {
            profile = activeProfileList.get(0);
        } else {
            // 同时存在多种运行环境
            throw new RuntimeException("同时存在环境变量：[" + StringUtils.arrayToCommaDelimitedString(activeProfiles) + "]");
        }
        String startJarPath = MsApplication.class.getResource("/").getPath().split("!")[0];
        String activePros = joinFun.apply(activeProfileList.toArray());
        System.out.printf("-----启动中，读取到的环境变量：[%s]，jar地址：[%s]%n", activePros, startJarPath);
        Properties props = System.getProperties();
        props.setProperty("spring.application.name", appName);
        props.setProperty("spring.profiles.active", profile);
        props.setProperty("info.version", AppConstant.APPLICATION_VERSION);
        props.setProperty("info.desc", appName);
        props.setProperty("ms.env", profile);
        props.setProperty("ms.name", appName);
        props.setProperty("ms.is-local", String.valueOf(isLocalDev()));
        props.setProperty("ms.dev-mode", profile.equals(AppConstant.PROD_MODE) ? "false" : "true");
        props.setProperty("ms.service.version", AppConstant.APPLICATION_VERSION);
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("spring.main.allow-bean-definition-overriding", "true");
        defaultProperties.setProperty("spring.sleuth.sampler.percentage", "1.0");
        defaultProperties.setProperty("spring.cloud.alibaba.seata.tx-service-group", appName.concat(NacosConstant.NACOS_GROUP_SUFFIX));
        defaultProperties.setProperty("spring.cloud.nacos.config.file-extension", NacosConstant.NACOS_CONFIG_FORMAT);
        defaultProperties.setProperty("spring.cloud.nacos.config.shared-configs[0].data-id", NacosConstant.sharedDataId());
        defaultProperties.setProperty("spring.cloud.nacos.config.shared-configs[0].group", NacosConstant.NACOS_CONFIG_GROUP);
        defaultProperties.setProperty("spring.cloud.nacos.config.shared-configs[0].refresh", NacosConstant.NACOS_CONFIG_REFRESH);
        defaultProperties.setProperty("spring.cloud.nacos.config.shared-configs[1].data-id", NacosConstant.sharedDataId(profile));
        defaultProperties.setProperty("spring.cloud.nacos.config.shared-configs[1].group", NacosConstant.NACOS_CONFIG_GROUP);
        defaultProperties.setProperty("spring.cloud.nacos.config.shared-configs[1].refresh", NacosConstant.NACOS_CONFIG_REFRESH);
        builder.properties(defaultProperties);
        // 加载自定义组件
        List<LauncherService> launcherList = new ArrayList<>();
        ServiceLoader.load(LauncherService.class).forEach(launcherList::add);
        launcherList.stream().sorted(Comparator.comparing(LauncherService::getOrder)).collect(Collectors.toList())
                .forEach(launcherService -> launcherService.launcher(builder, appName, profile,isLocalDev()));
        return builder;
    }

    public static boolean isLocalDev() {
        String osName = System.getProperty("os.name");
        return StringUtils.hasText(osName) && !(AppConstant.OS_NAME_LINUX.equalsIgnoreCase(osName));
    }
}
