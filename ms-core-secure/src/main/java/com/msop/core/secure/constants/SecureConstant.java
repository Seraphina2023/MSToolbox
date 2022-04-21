package com.msop.core.secure.constants;

/**
 * 授权校验常量
 *
 * @author ruozhuliufeng
 */
public interface SecureConstant {

    /**
     * 认证请求头
     */
    String BASIC_HEADER_KEY = "Auhorization";
    /**
     * 认证请求头前缀
     */
    String BASIC_HEADER_PREFIX = "Basic ";
    /**
     * 认证请求头前缀
     */
    String BAISC_HEADER_PREFIX_EXT = "Basic%20";

    /**
     * Redis 中授权token对应的key
     */
    String REDIS_TOKEN_AUTH = "auth:";
    /**
     * redis中应用对应的token集合的key
     */
    String REDIS_CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    /**
     * redis中用户名对应的token集合的key
     */
    String REDIS_UNAME_TO_ACCESS = "uname_to_access:";
    /**
     * rsa公钥
     */
    String RSA_PUBLIC_KEY = "pubkey.txt";
    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    String CACHE_CLIENT_KEY = "oauth_client_details";
    /**
     * 默认token过期时间(1小时)
     */
    Integer ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60;
    /**
     * 获取id_token的response_type
     */
    String ID_TOKEN = "id_token";
    /**
     * 账号类型参数名
     */
    String ACCOUNT_TYPE_PARAM_NAME = "accont_type";
}
