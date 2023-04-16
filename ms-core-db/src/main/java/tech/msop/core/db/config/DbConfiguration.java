package tech.msop.core.db.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tech.msop.core.db.properties.MsDbProperties;
//import tech.msop.core.launch.properties.MsPropertySource;

/**
 * 数据源配置类
 *
 * @author ruozhuliufeng
 */
@AutoConfiguration
@EnableConfigurationProperties(MsDbProperties.class)
//@MsPropertySource(value = "classpath:/ms-db.yml")
public class DbConfiguration {

}
