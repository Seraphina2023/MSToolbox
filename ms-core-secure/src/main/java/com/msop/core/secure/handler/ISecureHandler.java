package com.msop.core.secure.handler;

import com.msop.core.secure.properties.AuthSecure;
import com.msop.core.secure.properties.BasicSecure;
import com.msop.core.secure.properties.SignSecure;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

/**
 * secure 拦截器集合
 */
public interface ISecureHandler {
    /**
     * Token 拦截器
     * @return tokenInterceptor
     */
    HandlerInterceptorAdapter tokenInterceptor();

    /**
     * auth 拦截器
     * @param authSecures 授权集合
     * @return HandlerInterceptorAdapter
     */
    HandlerInterceptorAdapter authInterceptor(List<AuthSecure> authSecures);
    /**
     * basic 拦截器
     * @param basicSecures 授权集合
     * @return HandlerInterceptorAdapter
     */
    HandlerInterceptorAdapter basicInterceptor(List<BasicSecure> basicSecures);
    /**
     * sign 拦截器
     * @param signSecures 授权集合
     * @return HandlerInterceptorAdapter
     */
    HandlerInterceptorAdapter signInterceptor(List<SignSecure> signSecures);
    /**
     * client 拦截器
     * @param clientId 客户端ID
     * @return HandlerInterceptorAdapter
     */
    HandlerInterceptorAdapter clientInterceptor(String clientId);
}
