package tech.msop.core.loadbalancer.rule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;
import tech.msop.core.auto.annotation.AutoEnvPostProcessor;

/**
 * 灰度版本 自动处理
 */
@AutoEnvPostProcessor
public class GrayscaleEnvPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String GRAYSCALE_KEY = "ms.loadbalancer.version";
    private static final String METADATA_KEY = "spring.cloud.nacos.discovery.metadata.version";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String version = environment.getProperty(GRAYSCALE_KEY);
        if (StringUtils.hasText(version)) {
            environment.getSystemProperties().put(METADATA_KEY, version);
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
