package tech.msop.core.token.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import tech.msop.core.token.properties.MsTokenProperties;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * AES 对称加密 Jwt工具类
 */
public class AesJwtUtil {

    /**
     * token基础配置
     */
    public static String BEARER = "bearer";
    public static Integer AUTH_LENGTH = 7;

    /**
     * token保存至redis的key
     */
    private static final String REFRESH_TOKEN_CACHE = "ms:refreshToken";
    private static final String TOKEN_CACHE = "ms:token";
    private static final String TOKEN_KEY = "token:state:";

    /**
     * token 配置
     */
    private static MsTokenProperties tokenProperties;

    /**
     * redis工具
     */
    private static RedisTemplate<String, Object> redisTemplate;

    public static MsTokenProperties getJwtProperties() {
        return tokenProperties;
    }

    public static void setTokenProperties(MsTokenProperties properties) {
        if (AesJwtUtil.tokenProperties == null) {
            AesJwtUtil.tokenProperties = properties;
        }
    }

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        if (AesJwtUtil.redisTemplate == null) {
            AesJwtUtil.redisTemplate = redisTemplate;
        }
    }

    /**
     * 签名加密
     */
    public static String getBase64Security() {
        return Base64.getEncoder().encodeToString(getJwtProperties().getSignKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取请求传递的token串
     *
     * @param auth token
     * @return String
     */
    public static String getToken(String auth) {
        if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
            String headStr = auth.substring(0, 6).toLowerCase();
            if (headStr.compareTo(BEARER) == 0) {
                auth = auth.substring(7);
            }
            return auth;
        }
        return null;
    }

    /**
     * 解析jsonWebToken
     *
     * @param jsonWebToken token串
     * @return Claims
     */
    public static Claims parseJWT(String jsonWebToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Base64.getDecoder().decode(getBase64Security())).build()
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 获取保存在redis的accessToken
     *
     * @param tenantId    租户id
     * @param userId      用户id
     * @param accessToken accessToken
     * @return accessToken
     */
    public static String getAccessToken(String tenantId, String userId, String accessToken) {
        return String.valueOf(getRedisTemplate().opsForValue().get(getAccessTokenKey(tenantId, userId, accessToken)));
    }


    /**
     * 添加accessToken至redis
     *
     * @param tenantId    租户id
     * @param userId      用户id
     * @param accessToken accessToken
     * @param expire      过期时间
     */
    public static void addAccessToken(String tenantId, String userId, String accessToken, int expire) {
        getRedisTemplate().delete(getAccessTokenKey(tenantId, userId, accessToken));
        getRedisTemplate().opsForValue().set(getAccessTokenKey(tenantId, userId, accessToken), accessToken, expire, TimeUnit.SECONDS);
    }

    /**
     * 删除保存在redis的accessToken
     *
     * @param tenantId 租户id
     * @param userId   用户id
     */
    public static void removeAccessToken(String tenantId, String userId) {
        removeAccessToken(tenantId, userId, null);
    }

    /**
     * 删除保存在redis的accessToken
     *
     * @param tenantId    租户id
     * @param userId      用户id
     * @param accessToken accessToken
     */
    public static void removeAccessToken(String tenantId, String userId, String accessToken) {
        getRedisTemplate().delete(getAccessTokenKey(tenantId, userId, accessToken));
    }

    /**
     * 获取accessToken索引
     *
     * @param tenantId    租户id
     * @param userId      用户id
     * @param accessToken accessToken
     * @return token索引
     */
    public static String getAccessTokenKey(String tenantId, String userId, String accessToken) {
        String key = tenantId.concat(":").concat(TOKEN_CACHE).concat("::").concat(TOKEN_KEY);
        if (getJwtProperties().getSingle() || StringUtils.isEmpty(accessToken)) {
            return key.concat(userId);
        } else {
            return key.concat(accessToken);
        }
    }

    /**
     * 获取保存在redis的refreshToken
     *
     * @param tenantId     租户id
     * @param userId       用户id
     * @param refreshToken refreshToken
     * @return accessToken
     */
    public static String getRefreshToken(String tenantId, String userId, String refreshToken) {
        return String.valueOf(getRedisTemplate().opsForValue().get(getRefreshTokenKey(tenantId, userId)));
    }

    /**
     * 添加refreshToken至redis
     *
     * @param tenantId     租户id
     * @param userId       用户id
     * @param refreshToken refreshToken
     * @param expire       过期时间
     */
    public static void addRefreshToken(String tenantId, String userId, String refreshToken, int expire) {
        getRedisTemplate().delete(getRefreshTokenKey(tenantId, userId));
        getRedisTemplate().opsForValue().set(getRefreshTokenKey(tenantId, userId), refreshToken, expire, TimeUnit.SECONDS);
    }

    /**
     * 删除保存在refreshToken的token
     *
     * @param tenantId 租户id
     * @param userId   用户id
     */
    public static void removeRefreshToken(String tenantId, String userId) {
        getRedisTemplate().delete(getRefreshTokenKey(tenantId, userId));
    }

    /**
     * 获取refreshToken索引
     *
     * @param tenantId 租户id
     * @param userId   用户id
     * @return token索引
     */
    public static String getRefreshTokenKey(String tenantId, String userId) {
        return tenantId.concat(":").concat(REFRESH_TOKEN_CACHE).concat("::").concat(TOKEN_KEY).concat(userId);
    }

}
