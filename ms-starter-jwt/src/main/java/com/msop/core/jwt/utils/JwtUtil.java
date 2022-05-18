package com.msop.core.jwt.utils;

import com.msop.core.jwt.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * Jwt 工具类
 *
 * @author ruozhuliufeng
 */
public class JwtUtil {
    /**
     * token 基础配置
     */
    public static String BEARER = "bearer";
    public static Integer AUTH_LENGTH = 7;
    /**
     * token 保存至redis的key
     */
    public static final String TOKEN_CACHE = "ms:token";
    public static final String TOKEN_KEY = "token:state:";
    /**
     * jwt 配置
     */
    private static JwtProperties jwtProperties;
    /**
     * redis 工具
     */
    private static RedisTemplate<String, Object> redisTemplate;

    public static JwtProperties getJwtProperties() {
        return jwtProperties;
    }

    public static void setJwtProperties(JwtProperties jwtProperties) {
        if (JwtUtil.jwtProperties == null) {
            JwtUtil.jwtProperties = jwtProperties;
        }
    }

    public static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        if (JwtUtil.redisTemplate == null) {
            JwtUtil.redisTemplate = redisTemplate;
        }
    }

    /**
     * 签名加密
     *
     * @return 加密后的签名
     */
    public static String getBase64Security() {
        return Base64.getEncoder().encodeToString(getJwtProperties().getSignKey().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取请求传递的token串
     *
     * @param auth token
     * @return string
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
     * @param jsonWebToken token
     * @return claims
     */
    public static Claims parseJWT(String jsonWebToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(Base64.getDecoder().decode(getBase64Security())).build()
                    .parseClaimsJws(jsonWebToken)
                    .getBody();
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * 获取token 的索引
     *
     * @param tenantId    租户id
     * @param userId      用户id
     * @param accessToken token
     * @return token的索引
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
     * 获取保存在redis的token
     *
     * @param tenantId    租户id
     * @param userId      用户id
     * @param accessToken token
     * @return accessToken
     */
    public static String getAccessToken(String tenantId, String userId, String accessToken) {
        return String.valueOf(getRedisTemplate().opsForValue().get(getAccessTokenKey(tenantId, userId, accessToken)));
    }

    /**
     * 添加token至redis
     *
     * @param tenantId    租户id
     * @param userId      用户id
     * @param accessToken token
     * @param expire      过期时间
     */
    public static void addAccessToken(String tenantId, String userId, String accessToken, int expire) {
        getRedisTemplate().delete(getAccessTokenKey(tenantId, userId, accessToken));
        getRedisTemplate().opsForValue().set(getAccessTokenKey(tenantId, userId, accessToken), accessToken, expire, TimeUnit.SECONDS);
    }

    /**
     * 删除保存在redis 的token
     *
     * @param tenantId    租户id
     * @param userId      用户id
     * @param accessToken token
     */
    public static void removeAccessToken(String tenantId, String userId, String accessToken) {
        getRedisTemplate().delete(getAccessTokenKey(tenantId, userId, accessToken));
    }

    /**
     * 删除保存在redis 的token
     *
     * @param tenantId 租户id
     * @param userId   用户id
     */
    public static void removeAccessToken(String tenantId, String userId) {
        getRedisTemplate().delete(getAccessTokenKey(tenantId, userId, null));
    }
}
