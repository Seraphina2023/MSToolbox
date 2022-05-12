package com.msop.core.secure.interceptor;

import com.msop.core.common.constant.MsConstant;
import com.msop.core.common.jackson.JsonUtil;
import com.msop.core.common.model.CodeEnum;
import com.msop.core.common.model.Result;
import com.msop.core.common.utils.StringUtil;
import com.msop.core.common.utils.WebUtil;
import com.msop.core.secure.model.MsUser;
import com.msop.core.secure.utils.SecureUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Jwt拦截校验
 *
 * @author ruozhuliufeng
 */
@Slf4j
@AllArgsConstructor
public class SecureInterceptor implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (null != SecureUtil.getUser()) {
            return true;
        } else {
            log.warn("客户端认证失败，请求接口：{},请求IP：{},请求参数：{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
            Result result = Result.failed(CodeEnum.UN_AUTHORIZED);
            response.setHeader(MsConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(MsConstant.UTF_8);
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
            return false;
        }
    }
}
