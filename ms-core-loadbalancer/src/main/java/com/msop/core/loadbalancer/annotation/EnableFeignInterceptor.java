package com.msop.core.loadbalancer.annotation;

import com.msop.core.loadbalancer.config.FeignHttpInterceptorConfig;
import com.msop.core.loadbalancer.config.FeignInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启feign拦截器，传递数据给下游服务，包含基础数据和http的相关数据
 *
 * @author ruozhuliufeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({FeignHttpInterceptorConfig.class, FeignInterceptorConfig.class})
public @interface EnableFeignInterceptor {
}
