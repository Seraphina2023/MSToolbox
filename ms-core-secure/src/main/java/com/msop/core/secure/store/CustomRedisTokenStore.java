package com.msop.core.secure.store;

import com.msop.core.secure.constants.SecureConstant;
import com.msop.core.secure.properties.SecurityProperties;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 优化自Spring Security的{@link RedisTokenStore}
 * 1. 支持redis所有集群模式，包括cluster模式
 * 2. 使用pipeline减少连接次数，提升性能
 * 3. 自动续签token(可配置是否开启)
 *
 * @author ruozhuliufeng
 */
public class CustomRedisTokenStore implements TokenStore {
    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";

    private static final boolean springDataRedis_2_0 = ClassUtils.isPresent(
            "org.springframework.data.redis.connection.RedisStandaloneConfiguration",
            RedisTokenStore.class.getClassLoader());

    private final RedisConnectionFactory connectionFactory;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private RedisTokenStoreSerializationStrategy storeSerializationStrategy = new JdkSerializationStrategy();

    private String prefix = "";
    private Method redisConnetciontSet_2_0;

    private SecurityProperties securityProperties;

    private RedisSerializer<Object> redisSerializer;

    public CustomRedisTokenStore(RedisConnectionFactory connectionFactory, SecurityProperties securityProperties,
                                 RedisSerializer<Object> redisSerializer) {
        this.connectionFactory = connectionFactory;
        this.securityProperties = securityProperties;
        this.redisSerializer = redisSerializer;
        if (springDataRedis_2_0) {
            this.loadRedisConnectionMethods_2_0();
        }
    }

    public void setAuthenticationKeyGenerato(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    public void setStoreSerializationStrategy(RedisTokenStoreSerializationStrategy storeSerializationStrategy) {
        this.storeSerializationStrategy = storeSerializationStrategy;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void loadRedisConnectionMethods_2_0() {
        // 在类型clazz上，查询name方法，参数类型列表为paramTypes
        this.redisConnetciontSet_2_0 = ReflectionUtils.findMethod(
                RedisConnection.class, "set", byte[].class, byte[].class);
    }

    /**
     * 获取Redis连接
     *
     * @return Redis连接
     */
    private RedisConnection getConnection() {
        return connectionFactory.getConnection();
    }

    /**
     * 参数序列化
     *
     * @param object 待序列化的参数
     * @return 序列化后的数据
     */
    private byte[] serialize(Object object) {
        return storeSerializationStrategy.serialize(object);
    }

    /**
     * 参数序列化
     *
     * @param object 待序列化的参数
     * @return 序列化后的数据
     */
    private byte[] serializeKey(String object) {
        return serialize(prefix + object);
    }

    /**
     * 反序列化访问令牌
     *
     * @param bytes 令牌
     * @return 令牌反序列化后
     */
    private OAuth2AccessToken deserializeAccessToken(byte[] bytes) {
        return storeSerializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
    }

    /**
     * 反序列化身份验证
     *
     * @param bytes 身份验证
     * @return 身份验证反序列化后
     */
    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return storeSerializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    /**
     * 反序列化刷新令牌
     *
     * @param bytes 刷新令牌
     * @return 反序列化
     */
    private OAuth2RefreshToken deserializeRefreshToken(byte[] bytes) {
        return storeSerializationStrategy.deserialize(bytes, OAuth2RefreshToken.class);
    }

    /**
     * 反序列化客户端
     *
     * @param bytes 客户端
     * @return 反序列化
     */
    private ClientDetails deserializeClientDetails(byte[] bytes) {
        return (ClientDetails) redisSerializer.deserialize(bytes);
    }

    /**
     * 序列化字符串
     *
     * @param string 字符串
     * @return 序列化后的结果
     */
    private byte[] serialize(String string) {
        return storeSerializationStrategy.serialize(string);
    }

    private String deserializeString(byte[] bytes) {
        return storeSerializationStrategy.deserializeString(bytes);
    }


    /**
     * 检索针对提供的身份验证密钥存储的访问令牌
     *
     * @param oAuth2Authentication 身份认证信息
     * @return 令牌
     */
    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication oAuth2Authentication) {
        // 身份验证，并返回标识身份验证的唯一密钥
        String key = authenticationKeyGenerator.extractKey(oAuth2Authentication);
        byte[] serializeKey = serializeKey(AUTH_TO_ACCESS + key);
        byte[] bytes;
        // Java 7 build 105 版本开始，Java 7 的编译器和运行环境支持新的 try-with-resources 语句，称为 ARM 块(Automatic Resource Management) ，自动资源管理。
        //带有resources的try语句声明一个或多个resources。resources是在程序结束后必须关闭的对象。try-with-resources语句确保在语句末尾关闭每个resources。
        //任何实现java.lang.AutoCloseable,包括实现了java.io.Closeable的类，都可以作为resources使用。
        try (RedisConnection conn = getConnection()) {
            bytes = conn.get(serializeKey);
        }
        OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
        if (accessToken != null) {
            OAuth2Authentication storeAuthentication = readAuthentication(accessToken.getValue());
            // 判断令牌是否为空或身份验证是否符合
            if ((storeAuthentication == null || !key.equals(authenticationKeyGenerator.extractKey(storeAuthentication)))) {
                // 存储访问令牌
                // 保持身份一致，以免身份验证后有所更改
                storeAccessToken(accessToken, oAuth2Authentication);
            }
        }
        return accessToken;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication auth2Authentication = readAuthentication(token.getValue());
        // 是否开启token续签
        boolean isRenew = securityProperties.getAuth().getRenew().getEnable();
        if (isRenew && auth2Authentication != null) {
            OAuth2Request clientAuth = auth2Authentication.getOAuth2Request();
            // 判断当前应用是否需要自动续签
            if (checkRenewClientId(clientAuth.getClientId())) {
                // 读取过期时长
                int validitySeconds = getAccessTokenValiditySeconds(clientAuth.getClientId());
                if (validitySeconds > 0) {
                    double expiresRatio = token.getExpiresIn() / (double) validitySeconds;
                    // 判断是否需要续签，当前剩余时间小于过期时长的50%则续签(配置文件中配置)
                    if (expiresRatio <= securityProperties.getAuth().getRenew().getTimeRatio()) {
                        // 更新AccessToken过期时间
                        DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) token;
                        oAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
                        ;
                        // 更新AccessToken
                        storeAccessToken(oAuth2AccessToken, auth2Authentication, true);
                    }
                }
            }
        }
        return auth2Authentication;
    }


    @Override
    public OAuth2Authentication readAuthentication(String token) {
        byte[] bytes;
        try (RedisConnection conn = getConnection()) {
            bytes = conn.get(serializeKey(SecureConstant.REDIS_TOKEN_AUTH + token));
        }
        return deserializeAuthentication(bytes);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        storeAccessToken(token, authentication, false);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        byte[] key = serializeKey(ACCESS + tokenValue);
        byte[] bytes;
        try (RedisConnection conn = getConnection()) {
            bytes = conn.get(key);
        }
        return deserializeAccessToken(bytes);
    }

    /**
     * 移除令牌
     *
     * @param accessToken 令牌信息
     */
    @Override
    public void removeAccessToken(OAuth2AccessToken accessToken) {
        removeAccessToken(accessToken.getValue());
    }

    /**
     * 存储刷新令牌
     *
     * @param refreshToken   刷新令牌
     * @param authentication 登录信息
     */
    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        byte[] refreshKey = serializeKey(REFRESH + refreshToken.getValue());
        byte[] refreshAuthKey = serializeKey(REFRESH_AUTH + refreshToken.getValue());
        byte[] serializedRefreshToken = serialize(refreshToken);
        try (RedisConnection conn = getConnection()) {
            conn.openPipeline();
            if (springDataRedis_2_0) {
                try {
                    this.redisConnetciontSet_2_0.invoke(conn, refreshKey, serializedRefreshToken);
                    this.redisConnetciontSet_2_0.invoke(conn, refreshAuthKey, serialize(authentication));
                } catch (Exception exception) {
                    throw new RuntimeException(exception);
                }
            } else {
                conn.set(refreshKey, serializedRefreshToken);
                conn.set(refreshAuthKey, serialize(authentication));
            }
            expireRefreshToken(refreshToken, conn, refreshKey, refreshAuthKey);
            conn.closePipeline();
        }
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String token) {
        byte[] key = serializeKey(REFRESH + token);
        byte[] bytes;
        try (RedisConnection conn = getConnection()) {
            bytes = conn.get(key);
        }
        return deserializeRefreshToken(bytes);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        try (RedisConnection conn = getConnection()) {
            byte[] bytes;
            bytes = conn.get(serializeKey(REFRESH_AUTH + token));
            return deserializeAuthentication(bytes);
        }
    }


    @Override
    public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        removeRefreshToken(oAuth2RefreshToken.getValue());
    }

    private void removeRefreshToken(String value) {
        byte[] refreshKey = serializeKey(REFRESH + value);
        byte[] refreshAuthKey = serializeKey(REFRESH_AUTH + value);
        byte[] refresh2AccessKey = serializeKey(REFRESH_TO_ACCESS + value);
        byte[] access2RefreshKey = serializeKey(ACCESS_TO_REFRESH + value);
        try (RedisConnection conn = getConnection()) {
            conn.openPipeline();
            conn.del(refreshKey);
            conn.del(refresh2AccessKey);
            conn.del(refreshAuthKey);
            conn.del(access2RefreshKey);
            conn.closePipeline();
        }

    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        removeAccessTokenUsingRefreshToken(oAuth2RefreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String value) {
        byte[] key = serializeKey(REFRESH_TO_ACCESS + value);
        byte[] bytes;
        try (RedisConnection conn = getConnection()) {
            bytes = conn.get(key);
            conn.del(key);
        }
        if (bytes == null) {
            return;
        }
        String accessToken = deserializeString(bytes);
        if (accessToken != null) {
            removeAccessToken(accessToken);
        }
    }

    private void removeAccessToken(String tokenValue) {
        byte[] accessKey = serializeKey(ACCESS + tokenValue);
        byte[] authKey = serializeKey(SecureConstant.REDIS_TOKEN_AUTH + tokenValue);
        byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + tokenValue);
        try (RedisConnection conn = getConnection()) {
            byte[] access = conn.get(accessKey);
            byte[] auth = conn.get(authKey);
            conn.openPipeline();
            conn.del(accessKey);
            conn.del(accessToRefreshKey);
            // 不要删除刷新令牌 - 这取决于调用者
            conn.del(authKey);
            conn.closePipeline();

            OAuth2Authentication authentication = deserializeAuthentication(auth);
            if (authentication != null) {
                String key = authenticationKeyGenerator.extractKey(authentication);
                byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + key);
                byte[] unameKey = serializeKey(SecureConstant.REDIS_UNAME_TO_ACCESS + getApprovalKey(authentication));
                byte[] clientId = serializeKey(SecureConstant.REDIS_CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());
                conn.openPipeline();
                conn.del(authToAccessKey);
                conn.lRem(unameKey, 1, access);
                conn.lRem(clientId, 1, access);
                conn.del(serialize(ACCESS + key));
                conn.closePipeline();
            }
        }

    }


    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        byte[] key = serializeKey(SecureConstant.REDIS_UNAME_TO_ACCESS + getApprovalKey(clientId, userName));
        List<byte[]> byteList;
        try (RedisConnection conn = getConnection()) {
            byteList = conn.lRange(key, 0, -1);
        }
        return getTokenCollections(byteList);
    }


    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        byte[] key = serializeKey(SecureConstant.REDIS_CLIENT_ID_TO_ACCESS + clientId);
        List<byte[]> byteList;
        try (RedisConnection conn = getConnection()) {
            byteList = conn.lRange(key, 0, -1);
        }
        return getTokenCollections(byteList);
    }


    /**
     * 判断应用自动续签是否满足黑名单和白名单的过滤逻辑
     *
     * @param clientId 应用id
     * @return 是否符合条件
     */
    private boolean checkRenewClientId(String clientId) {
        boolean result = true;
        // 白名单
        List<String> includeClientIds = securityProperties.getAuth().getRenew().getIncludeClientIds();
        // 黑名单
        List<String> exclusiveClientIds = securityProperties.getAuth().getRenew().getExclusiveClientIds();
        if (includeClientIds.size() > 0) {
            result = includeClientIds.contains(clientId);
        } else if (exclusiveClientIds.size() > 0) {
            result = !exclusiveClientIds.contains(clientId);
        }
        return result;
    }

    /**
     * 获取token的剩余有效时长
     *
     * @param clientId 应用id
     * @return
     */
    private int getAccessTokenValiditySeconds(String clientId) {
        byte[] bytes;
        try (RedisConnection conn = getConnection()) {
            bytes = conn.get(serializeKey(SecureConstant.CACHE_CLIENT_KEY + ":" + clientId));
        }
        if (bytes != null) {
            ClientDetails clientDetails = deserializeClientDetails(bytes);
            if (clientDetails.getAccessTokenValiditySeconds() != null) {
                return clientDetails.getAccessTokenValiditySeconds();
            }
        }
        // 返回默认值
        return SecureConstant.ACCESS_TOKEN_VALIDITY_SECONDS;
    }

    /**
     * 存储token
     *
     * @param token          访问令牌
     * @param authentication 身份信息
     * @param isRenew        是否续签
     */
    private void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication, boolean isRenew) {
        byte[] serializedAccessToken = serialize(token);
        byte[] serializedAuth = serialize(authentication);
        byte[] accessKey = serializeKey(ACCESS + token.getValue());
        byte[] authKey = serializeKey(SecureConstant.REDIS_TOKEN_AUTH + token.getValue());
        byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
        byte[] approvalKey = serializeKey(SecureConstant.REDIS_TOKEN_AUTH + getApprovalKey(authentication));
        byte[] clientId = serializeKey(SecureConstant.REDIS_CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());

        try (RedisConnection conn = getConnection()) {
            byte[] oldAccessToken = conn.get(accessKey);
            // 如果token以存在，并且不是续签的话直接返回
            if (!isRenew && oldAccessToken != null) {
                return;
            }
            conn.openPipeline();
            if (springDataRedis_2_0) {
                try {
                    this.redisConnetciontSet_2_0.invoke(conn, accessKey, serializedAccessToken);
                    this.redisConnetciontSet_2_0.invoke(conn, authKey, serializedAuth);
                    this.redisConnetciontSet_2_0.invoke(conn, authToAccessKey, serializedAccessToken);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                conn.set(accessKey, serializedAccessToken);
                conn.set(authKey, serializedAuth);
                conn.set(authToAccessKey, serializedAccessToken);
            }
            // 如果是续签token,需要先删除集合里旧的值
            if (oldAccessToken != null) {
                if (!authentication.isClientOnly()) {
                    conn.lRem(approvalKey, 1, oldAccessToken);
                }
                conn.lRem(clientId, 1, oldAccessToken);
            }
            if (!authentication.isClientOnly()) {
                conn.rPush(approvalKey, serializedAccessToken);
            }
            conn.rPush(clientId, serializedAccessToken);
            if (token.getExpiration() != null) {
                int seconds = token.getExpiresIn();
                conn.expire(accessKey, seconds);
                conn.expire(authKey, seconds);
                conn.expire(authToAccessKey, seconds);
                conn.expire(clientId, seconds);
                conn.expire(approvalKey, seconds);
            }
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken != null && refreshToken.getValue() != null) {
                byte[] refresh = serialize(token.getRefreshToken().getValue());
                byte[] auth = serialize(token.getValue());
                byte[] refreshToAccessKey = serializeKey(REFRESH_TO_ACCESS + token.getRefreshToken().getValue());
                byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + token.getValue());
                if (springDataRedis_2_0) {
                    try {
                        this.redisConnetciontSet_2_0.invoke(conn, refreshToAccessKey, auth);
                        this.redisConnetciontSet_2_0.invoke(conn, accessToRefreshKey, refresh);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    conn.set(refreshToAccessKey, auth);
                    conn.set(accessToRefreshKey, refresh);
                }
                expireRefreshToken(refreshToken, conn, refreshToAccessKey, accessToRefreshKey);
            }
            conn.closePipeline();
        }
    }

    private void expireRefreshToken(OAuth2RefreshToken refreshToken, RedisConnection conn, byte[] refreshToAccessKey, byte[] accessToRefreshKey) {
        if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
            ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
            Date expiration = expiringRefreshToken.getExpiration();
            if (expiration != null) {
                int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                        .intValue();
                conn.expire(refreshToAccessKey, seconds);
                conn.expire(accessToRefreshKey, seconds);
            }
        }
    }

    private String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? ""
                : authentication.getUserAuthentication().getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private Collection<OAuth2AccessToken> getTokenCollections(List<byte[]> byteList) {
        if (byteList == null || byteList.size() == 0) {
            return Collections.emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<>(byteList.size());
        for (byte[] bytes : byteList) {
            OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
            accessTokens.add(accessToken);
        }
        return Collections.unmodifiableCollection(accessTokens);
    }

    private String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }
}
