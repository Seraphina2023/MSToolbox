package com.msop.core.social.config;

import com.msop.core.launch.properties.MsPropertySource;
import com.msop.core.redis.config.RedisTemplateConfiguration;
import com.msop.core.social.cache.AuthStateRedisCache;
import com.msop.core.social.properties.SocialProperties;
import com.xkcoding.http.HttpUtil;
import com.xkcoding.http.support.Http;
import com.xkcoding.http.support.httpclient.HttpClientImpl;
import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * SocialConfiguration
 *
 * @author ruozhuliufeng
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SocialProperties.class)
@AutoConfigureAfter(RedisTemplateConfiguration.class)
@MsPropertySource(value = "classpath:/ms-social.yml")
public class SocialConfiguration {

	@Bean
	@ConditionalOnMissingBean(Http.class)
	public Http simpleHttp() {
		HttpClientImpl httpClient = new HttpClientImpl();
		HttpUtil.setHttp(httpClient);
		return httpClient;
	}

	@Bean
	@ConditionalOnMissingBean(AuthStateCache.class)
	public AuthStateCache authStateCache(RedisTemplate<String, Object> redisTemplate) {
		return new AuthStateRedisCache(redisTemplate, redisTemplate.opsForValue());
	}

}
