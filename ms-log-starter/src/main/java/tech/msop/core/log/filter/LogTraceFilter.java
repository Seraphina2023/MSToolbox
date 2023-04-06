package tech.msop.core.log.filter;

import tech.msop.core.log.utils.LogTraceUtil;

import javax.servlet.*;
import java.io.IOException;

/**
 * 日志追踪过滤器
 *
 * @author ruozhuliufeng
 */
public class LogTraceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        boolean flag = LogTraceUtil.insert();
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (flag) {
                LogTraceUtil.remove();
            }
        }
    }

    @Override
    public void destroy() {
    }
}
