package com.msop.core.secure.handler;

import com.msop.core.secure.interceptor.*;
import com.msop.core.secure.properties.AuthSecure;
import com.msop.core.secure.properties.BasicSecure;
import com.msop.core.secure.properties.SignSecure;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

/**
 * Secure 处理
 *
 * @author ruozhuliufeng
 */
public class SecureHandler implements ISecureHandler{
    /**
     * Token 拦截器
     *
     * @return tokenInterceptor
     */
    @Override
    public HandlerInterceptorAdapter tokenInterceptor() {
        return new TokenInterceptor();
    }

    /**
     * auth 拦截器
     *
     * @param authSecures 授权集合
     * @return HandlerInterceptorAdapter
     */
    @Override
    public HandlerInterceptorAdapter authInterceptor(List<AuthSecure> authSecures) {
        return new AuthInterceptor(authSecures);
    }

    /**
     * basic 拦截器
     *
     * @param basicSecures 授权集合
     * @return HandlerInterceptorAdapter
     */
    @Override
    public HandlerInterceptorAdapter basicInterceptor(List<BasicSecure> basicSecures) {
        return new BasicInterceptor(basicSecures);
    }

    /**
     * sign 拦截器
     *
     * @param signSecures 授权集合
     * @return HandlerInterceptorAdapter
     */
    @Override
    public HandlerInterceptorAdapter signInterceptor(List<SignSecure> signSecures) {
        return new SignInterceptor(signSecures);
    }

    /**
     * client 拦截器
     *
     * @param clientId 客户端ID
     * @return HandlerInterceptorAdapter
     */
    @Override
    public HandlerInterceptorAdapter clientInterceptor(String clientId) {
        return new ClientInterceptor(clientId);
    }
}
