package tech.msop.core.token.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import tech.msop.core.token.properties.MsTokenProperties;
import tech.msop.core.token.serializer.JwtRedisKeySerializer;
import tech.msop.core.token.util.AesJwtUtil;

/**
 * Token 配置类
 */
@AutoConfiguration
@AllArgsConstructor
@EnableConfigurationProperties(MsTokenProperties.class)
public class MsTokenConfiguration implements SmartInitializingSingleton {
    private final MsTokenProperties properties;
    private final RedisConnectionFactory redisConnectionFactory;


    @Override
    public void afterSingletonsInstantiated() {
        // RedisTemplate 实例化
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        JwtRedisKeySerializer redisKeySerializer = new JwtRedisKeySerializer();
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        // key 序列化
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);
        // value 序列化
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setHashValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        AesJwtUtil.setTokenProperties(properties);
        AesJwtUtil.setRedisTemplate(redisTemplate);
    }
}
