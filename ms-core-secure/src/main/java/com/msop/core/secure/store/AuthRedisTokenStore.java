package com.msop.core.secure.store;

import com.msop.core.secure.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * 认证服务器使用Redis存取令牌，注意：需要配置redis参数
 * @author ruozhuliufeng
 * @date 2021-08-07
 * 注解注释：@ConditionalOnProperty 控制配置类是否生效，使用该注解来控制@Configuration是否生效
 *  参数： value:数组，获取对应property名称的值，与name不可同时使用
 *        prefix:配置属性名称的前缀
 *        name: 数组，配置属性完整名称或部分名称，可与prefix组合使用，组成完整的配置属性名称，与value不可同时使用
 *        havingValue: 可与name组合使用，比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置
 *        matchIfMissing: 缺少该配置属性时是否可以加载。如果为true，没有该配置属性时也会正常加载，反之则不会生效
 */
@Configuration
@ConditionalOnProperty(prefix = "ms.oauth2.token.store",name = "type",havingValue = "redis",matchIfMissing = true)
public class AuthRedisTokenStore {

    @Bean
    public TokenStore tokenStore(RedisConnectionFactory connectionFactory, SecurityProperties securityProperties,
                                 RedisSerializer<Object> redisSerializer){
        return new CustomRedisTokenStore(connectionFactory, securityProperties,redisSerializer);
    }
}
