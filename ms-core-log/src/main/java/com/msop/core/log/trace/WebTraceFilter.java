package com.msop.core.log.trace;

import cn.hutool.core.util.StrUtil;
import com.msop.core.log.properties.TraceProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * web过滤器，生成日志链路追踪id，并赋值MDC
 *
 * @author ruozhuliufeng
 * 注解注释：@Order 排序，值越低越靠前
 */
@Component
@ConditionalOnClass(value = {HttpServletRequest.class, OncePerRequestFilter.class})
@Order(value = MDCTraceUtils.FILTER_ORDER)
public class WebTraceFilter extends OncePerRequestFilter {
    @Resource
    private TraceProperties traceProperties;

    /**
     * 判断是否过滤
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !traceProperties.getEnable();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = httpServletRequest.getHeader(MDCTraceUtils.TRACE_ID_HEADER);
            String spanId = httpServletRequest.getHeader(MDCTraceUtils.SPAN_ID_HEADER);
            if (StrUtil.isEmpty(traceId)) {
                MDCTraceUtils.addTraceId();
            } else {
                MDCTraceUtils.putTraceId(traceId, spanId);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            MDCTraceUtils.removeTraceId();
        }
    }
}
