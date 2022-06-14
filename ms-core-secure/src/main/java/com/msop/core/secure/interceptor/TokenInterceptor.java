package com.msop.core.secure.interceptor;

import com.msop.core.secure.provider.ResponseProvider;
import com.msop.core.secure.utils.AuthUtil;
import com.msop.core.tool.jackson.JsonUtil;
import com.msop.core.tool.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token 拦截器校验
 *
 * @author ruozhuliufeng
 */
@Slf4j
@AllArgsConstructor
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (null == AuthUtil.getUser()) {
            log.warn("授权认证失败,请求接口:{},请求IP:{},请求参数:{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
            ResponseProvider.write(response);
            return false;
        }
        return true;
    }
}
