package com.msop.core.context;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpHeaders 获取器，用于跨服务和线程的传递
 * 暂不支持webflux
 *
 * @author ruozhuliufeng
 */
public interface MsHttpHeadersGetter {
    /**
     * 获取HttpHeaders
     *
     * @return HttpHeaders
     */
    @Nullable
    HttpHeaders get();

    /**
     * 获取HttpHeaders
     *
     * @param request 请求
     * @return HttpHeaders
     */
    @Nullable
    HttpHeaders get(HttpServletRequest request);
}
