package tech.msop.core.db.config;

import tech.msop.core.launch.properties.MsPropertySource;
import org.springframework.context.annotation.Configuration;

/**
 * 数据源配置类
 *
 * @author ruozhuliufeng
 */
@Configuration
@MsPropertySource(value = "classpath:/ms-db.yml")
public class DbConfiguration {

}
