package com.msop.core.jwt.config;

import com.msop.core.jwt.properties.JwtProperties;
import com.msop.core.jwt.serializer.JwtRedisKeySerializer;
import com.msop.core.jwt.utils.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

/**
 * Jwt 配置类
 *
 * @author ruozhuliufeng
 */
@Configuration
@AllArgsConstructor
@AutoConfigureAfter(JwtRedisConfiguration.class)
@EnableConfigurationProperties({JwtProperties.class})
public class JwtConfiguration implements SmartInitializingSingleton {
    private final JwtProperties jwtProperties;
    private RedisConnectionFactory redisConnectionFactory;


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
        JwtUtil.setJwtProperties(jwtProperties);
        JwtUtil.setRedisTemplate(redisTemplate);
    }
}
