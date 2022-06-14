package com.msop.core.secure.interceptor;

import com.msop.core.secure.model.MsUser;
import com.msop.core.secure.provider.ResponseProvider;
import com.msop.core.secure.utils.SecureUtil;
import com.msop.core.tool.constant.MsConstant;
import com.msop.core.tool.jackson.JsonUtil;
import com.msop.core.tool.model.CodeEnum;
import com.msop.core.tool.model.Result;
import com.msop.core.tool.utils.StringUtil;
import com.msop.core.tool.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 客户端校验
 *
 * @author ruozhuliufeng
 */
@Slf4j
@AllArgsConstructor
public class ClientInterceptor extends HandlerInterceptorAdapter {
    private final String clientId;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull Object handler) throws Exception {
        MsUser user = SecureUtil.getUser();
        if (user != null
                && StringUtil.equals(clientId, SecureUtil.getClientIdFromHeader())
                && StringUtil.equals(clientId, user.getClientId())) {
            return true;
        } else {
            log.warn("客户端认证失败，请求接口：{},请求IP：{},请求参数：{}",request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
            ResponseProvider.write(response);
            return false;
        }
    }
}
