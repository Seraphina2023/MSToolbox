package tech.msop.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * MS 安全配置
 *
 * @author ruozhuliufeng
 */
@Data
@ConfigurationProperties(prefix = "ms.security")
@RefreshScope
public class MsSecurityProperties {
    private AuthProperties auth = new AuthProperties();

    private PermitProperties ignore = new PermitProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();
}
