package tech.msop.core.token.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Token 类型与风格
 */
@Data
@ConfigurationProperties("ms.token")
public class MsTokenProperties {
    /**
     * Token 风格
     */
    private String tokenStyle;
    private String timeout;
}
