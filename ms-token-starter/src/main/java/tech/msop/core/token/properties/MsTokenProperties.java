package tech.msop.core.token.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import tech.msop.core.token.constants.TokenConstant;

/**
 * Token 类型与风格
 */
@Data
@ConfigurationProperties("ms.token")
public class MsTokenProperties {
    /**
     * Token 风格
     */
    private String tokenStyle = TokenConstant.TOKEN_STYLE_AES_JWT;
    /**
     * token 是否有状态
     */
    private Boolean state = Boolean.FALSE;
    /**
     * 是否只可同时在线一人
     */
    private Boolean single = Boolean.FALSE;

    /**
     * token 签名
     */
    private String signKey = TokenConstant.DEFAULT_SECRET_KEY;

    /**
     * 获取签名规则
     *
     * @return 签名
     */
    public String getSignKey() {
        if (this.signKey.length() < TokenConstant.SECRET_KEY_LENGTH) {
            return TokenConstant.DEFAULT_SECRET_KEY;
        }
        return this.signKey;
    }
}
