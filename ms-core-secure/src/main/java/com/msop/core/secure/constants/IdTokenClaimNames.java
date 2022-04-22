package com.msop.core.secure.constants;

/**
 * id_token属性名常量
 *
 * @author ruozhuliufneg
 */
public interface IdTokenClaimNames {
    /**
     * {@code iss} - the Issuer identifier
     */
    String ISS = "iss";

    /**
     * {@code sub} - the Subject identifier
     */
    String SUB = "sub";

    /**
     * {@code aud} - the Audience(s) that the ID Token is intended for
     */
    String AUD = "aud";

    /**
     * {@code exp} - the Expiration time on or after which the ID Token MUST NOT be accepted
     */
    String EXP = "exp";

    /**
     * {@code iat} - the time at which the ID Token was issued
     */
    String IAT = "iat";

    /**
     * {@code auth_time} - the time when the End-User authentication occurred
     */
    String AUTH_TIME = "auth_time";

    /**
     * {@code nonce} - a {@code String} value used to associate a Client session with an ID Token,
     * and to mitigate replay attacks.
     */
    String NONCE = "nonce";

    /**
     * {@code acr} - the Authentication Context Class Reference
     */
    String ACR = "acr";

    /**
     * {@code amr} - the Authentication Methods References
     */
    String AMR = "amr";

    /**
     * {@code azp} - the Authorized party to which the ID Token was issued
     */
    String AZP = "azp";

    /**
     * {@code at_hash} - the Access Token hash value
     */
    String AT_HASH = "at_hash";

    /**
     * {@code c_hash} - the Authorization Code hash value
     */
    String C_HASH = "c_hash";

    /**
     * {@code name} - 用户姓名
     */
    String NAME = "name";

    /**
     * {@code login_name} - 登录名
     */
    String L_NAME = "login_name";

    /**
     * {@code picture} - 头像照片
     */
    String PIC = "picture";
}
