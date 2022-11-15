package tech.msop.core.jwt.constant;

/**
 * JWT 常量
 *
 * @author ruozhuliufeng
 */
public interface JwtConstant {

    /**
     * 默认Key
     */
    String DEFAULT_SECRET_KEY = "mstoolboxmeansmicroservicetoolboxwhichishelpdeveloperfastercode";

    /**
     * key 安全长度，具体见：https://tools.ietf.org/html/rfc7518#section-3.2
     */
    int SERCRET_KEY_LENGTH = 32;
}
