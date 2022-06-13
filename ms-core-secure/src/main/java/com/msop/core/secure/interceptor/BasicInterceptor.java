package com.msop.core.secure.interceptor;

import com.msop.core.secure.constant.SecureConstant;
import com.msop.core.secure.properties.AuthSecure;
import com.msop.core.secure.properties.BasicSecure;
import com.msop.core.secure.provider.HttpMethod;
import com.msop.core.secure.provider.ResponseProvider;
import com.msop.core.secure.utils.SecureUtil;
import com.msop.core.tool.jackson.JsonUtil;
import com.msop.core.tool.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 基础认证拦截器校验
 *
 * @author ruozhuliufeng
 */
@Slf4j
@AllArgsConstructor
public class BasicInterceptor extends HandlerInterceptorAdapter {
    /**
     * 表达式匹配
     */
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    /**
     * 授权集合
     */
    private final List<BasicSecure> basicSecures;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) throws Exception {
        boolean check = basicSecures.stream()
                .filter(basicSecure -> checkAuth(request, basicSecure))
                .findFirst()
                .map(basicSecure -> checkBasic(basicSecure.getUsername(),basicSecure.getPassword()))
                .orElse(Boolean.TRUE);
        if (!check) {
            log.warn("授权认证失败,请求接口:{},请求IP:{},请求参数:{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
            response.setHeader(SecureConstant.BASIC_REALM_HEADER_KEY,SecureConstant.BASIC_REALM_HEADER_VALUE);
            ResponseProvider.write(response);
            return false;
        }
        return true;
    }

    /**
     * 检测授权
     */
    private boolean checkAuth(HttpServletRequest request, BasicSecure basicSecure) {
        return checkMethod(request, basicSecure.getMethod()) && checkPath(request, basicSecure.getPattern());
    }

    /**
     * 检测请求方法
     */
    private boolean checkMethod(HttpServletRequest request, HttpMethod method) {
        return method == HttpMethod.ALL ||
                (method != null && method == HttpMethod.of(request.getMethod()));
    }

    /**
     * 检测路径匹配
     */
    private boolean checkPath(HttpServletRequest request, String pattern) {
        String servletPath = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 0) {
            servletPath = servletPath + pathInfo;
        }
        return ANT_PATH_MATCHER.match(pattern, servletPath);
    }

    /**
     * 检测表达式
     */
    private boolean checkBasic(String username,String password) {
        try{
            String[] tokens = SecureUtil.extractAndDecodeHeader();
            return username.equals(tokens[0]) && password.equals(tokens[1]);
        }catch (Exception e){
            log.warn("授权认证失败，错误信息：{}",e.getMessage());
            return false;
        }
    }
}
