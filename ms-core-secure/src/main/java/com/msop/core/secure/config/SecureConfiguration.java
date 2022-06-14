package com.msop.core.secure.config;

import com.msop.core.secure.aspect.AuthAspect;
import com.msop.core.secure.handler.ISecureHandler;
import com.msop.core.secure.properties.AuthSecure;
import com.msop.core.secure.properties.BasicSecure;
import com.msop.core.secure.properties.MsSecureProperties;
import com.msop.core.secure.properties.SignSecure;
import com.msop.core.secure.provider.ClientDetailsServiceImpl;
import com.msop.core.secure.provider.IClientDetailsService;
import com.msop.core.secure.registry.SecureRegistry;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 安全配置类
 *
 * @author ruozhuliufeng
 */
@Order
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@EnableConfigurationProperties({MsSecureProperties.class})
public class SecureConfiguration implements WebMvcConfigurer {

    private final SecureRegistry secureRegistry;
    private final MsSecureProperties secureProperties;

    private final JdbcTemplate jdbcTemplate;

    private final ISecureHandler secureHandler;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 设置请求授权
        if (secureRegistry.isAuthEnabled() || secureProperties.getAuthEnabled()){
            List<AuthSecure> authSecures = this.secureRegistry.addAuthPatterns(secureProperties.getAuth()).getAuthSecures();
            if (authSecures.size() > 0){
                registry.addInterceptor(secureHandler.authInterceptor(authSecures));
                // 设置路径放行
                secureRegistry.excludePathPatterns(authSecures.stream().map(AuthSecure::getPattern).collect(Collectors.toList()));
            }
        }
        // 设置基础认证授权
        if (secureRegistry.isBasicEnabled() || secureProperties.getBasicEnabled()){
            List<BasicSecure> basicSecures = this.secureRegistry.addBasicPatterns(secureProperties.getBasic()).getBasicSecures();
            if (basicSecures.size() > 0){
                registry.addInterceptor(secureHandler.basicInterceptor(basicSecures));
                // 设置路径放行
                secureRegistry.excludePathPatterns(basicSecures.stream().map(BasicSecure::getPattern).collect(Collectors.toList()));
            }
        }
        // 设置签名认证授权
        if (secureRegistry.isSignEnabled() || secureProperties.getSignEnabled()){
            List<SignSecure> signSecures = this.secureRegistry.addSignPatterns(secureProperties.getSign()).getSignSecures();
            if (signSecures.size() > 0){
                registry.addInterceptor(secureHandler.signInterceptor(signSecures));
                // 设置路径放行
                secureRegistry.excludePathPatterns(signSecures.stream().map(SignSecure::getPattern).collect(Collectors.toList()));
            }
        }
        // 设置客户端授权
        if (secureRegistry.isClientEnabled() || secureProperties.getClientEnabled()){
            secureProperties.getClient().forEach(
                    clientSecure -> registry.addInterceptor(secureHandler.clientInterceptor(clientSecure.getClientId()))
                            .addPathPatterns(clientSecure.getPathPatterns())
            );
        }
        // 设置路径放行
        if (secureRegistry.isEnabled() || secureProperties.getEnabled()) {
            registry.addInterceptor(secureHandler.tokenInterceptor())
                    .excludePathPatterns(secureRegistry.getExcludePatterns())
                    .excludePathPatterns(secureRegistry.getDefaultExcludePatterns())
                    .excludePathPatterns(secureProperties.getSkipUrl());
        }
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Bean
    public AuthAspect authAspect() {
        return new AuthAspect();
    }

    @Bean
    @ConditionalOnMissingBean(IClientDetailsService.class)
    public IClientDetailsService clientDetailsService() {
        return new ClientDetailsServiceImpl(jdbcTemplate);
    }

}
