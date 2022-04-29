package com.msop.core.secure.store;

import com.msop.core.common.utils.StringUtil;
import com.msop.core.secure.constants.SecureConstant;
import com.msop.core.secure.converter.CustomUserAuthenticationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 资源服务器TokenStore配置类，使用Jwt RSA 非对称加密
 *
 * @author ruozhuliufeng
 */
@Configuration
@ConditionalOnProperty(prefix = "ms.oauth2.token.store", name = "type", havingValue = "resJwt")
public class ResJwtTokenStore {

    @Autowired
    private ResourceServerProperties resource;

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter accessTokenConverter) {
        return new JwtTokenStore(accessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifierKey(getPubKey());
        DefaultAccessTokenConverter tokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
        tokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
        return converter;
    }

    /**
     * 获取非对称加密秘钥 key
     *
     * @return 公钥Key
     */
    private String getPubKey() {
        Resource resource = new ClassPathResource(SecureConstant.RSA_PUBLIC_KEY);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return getKeyFromAuthorizationServer();
        }
    }

    /**
     * 通过访问授权服务器获取非对称加密公钥Key
     *
     * @return 公钥Key
     */
    private String getKeyFromAuthorizationServer() {
        if (!StringUtil.isEmpty(this.resource.getJwt().getKeyUri())) {
            final HttpHeaders headers = new HttpHeaders();
            final String username = this.resource.getClientId();
            final String password = this.resource.getClientSecret();
            if (username != null && password != null) {
                final byte[] token = Base64.getEncoder().encode((username + ":" + password).getBytes());
                // 添加授权信息
                headers.add("Authorization", "Basic " + new String(token));
            }
            final HttpEntity<Void> request = new HttpEntity<>(headers);
            final String url = this.resource.getJwt().getKeyUri();
            return (String) new RestTemplate()
                    .exchange(url, HttpMethod.GET, request, Map.class).getBody()
                    .get("value");
        }
        return null;
    }
}
