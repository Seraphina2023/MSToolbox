package tech.msop.core.token.constants;

/**
 * Token 常量
 */
public interface TokenConstant {

    //============================ Token 风格常量  =================================
    /**
     * Token风格: uuid
     */
    String TOKEN_STYLE_UUID = "uuid";

    /**
     * Token风格: 简单uuid (不带下划线)
     */
    String TOKEN_STYLE_SIMPLE_UUID = "simple-uuid";

    /**
     * Token风格: 32位随机字符串
     */
    String TOKEN_STYLE_RANDOM_32 = "random-32";

    /**
     * Token风格: 64位随机字符串
     */
    String TOKEN_STYLE_RANDOM_64 = "random-64";

    /**
     * Token风格: 128位随机字符串
     */
    String TOKEN_STYLE_RANDOM_128 = "random-128";

    /**
     * Token风格: tik风格 (2_14_16)
     */
    String TOKEN_STYLE_TIK = "tik";
    /**
     * Token风格: 认证服务器使用 RSA 非对称加密 JWT
     */
    String TOKEN_STYLE_AUTH_JWT = "auth-jwt";
    /**
     * Token风格: 资源服务器使用 RSA 非对称加密 JWT
     */
    String TOKEN_STYLE_RES_JWT = "res-jwt";
    /**
     * Token风格: AES 对称加密 JWT
     */
    String TOKEN_STYLE_AES_JWT = "aes-jwt";

    //============================ JWT 相关常量  =================================
    /**
     * rsa公钥
     */
    String RSA_PUBLIC_KEY = "pubkey.txt";
    /**
     * 默认key
     */
    String DEFAULT_SECRET_KEY = "mstoolboxisapowerfulmicroservicetool";
    /**
     * key 安全长度，具体见：<a href="https://tools.ietf.org/html/rfc7518#section-3.2">...</a>
     */
    int SECRET_KEY_LENGTH = 32;
}
