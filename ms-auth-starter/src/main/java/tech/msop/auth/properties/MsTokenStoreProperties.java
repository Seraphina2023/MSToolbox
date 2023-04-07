package tech.msop.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Data
@ConfigurationProperties(prefix = "ms.token.store")
@RefreshScope
public class MsTokenStoreProperties {
    /**
     * Token 存储类型 (redis/db/authJwt/resJwt/jwt/custom)<br/>
     *
     *
     *  redis: Token存储在Redis中,支持续签,默认<br/>
     *  db: Token存储到数据库中,暂不支持续签<br/>
     *  authJwt: 认证服务器JWT,RSA 非对称加密，暂不支持续签<br/>
     *  resJwt: 资源服务器JWT,RSA 非对称加密,暂不支持续签<br/>
     *  jwt: JWT,AES 对称加密,暂不支持续签<br/>
     *  custom: 自定义风格的UUID格式,暂不支持续签<br/>
     */
    private String type = "redis";
}
