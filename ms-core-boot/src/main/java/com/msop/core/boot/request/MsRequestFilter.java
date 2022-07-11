package com.msop.core.boot.request;

import lombok.AllArgsConstructor;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Request 全局过滤
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
public class MsRequestFilter implements Filter {

    private final RequestProperties requestProperties;
    private final XssProperties xssProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String path = ((HttpServletRequest) servletRequest).getServletPath();
        // 跳过 Request 包装
        if (!requestProperties.getEnabled() || isRequestSkip(path)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        // 默认 Request 包装
        else if (!xssProperties.getEnabled() || isXssSkip(path)) {
            MsHttpServletRequestWrapper msRequest = new MsHttpServletRequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(msRequest, servletResponse);
        }
        // Xss Request 包装
        else {
            XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) servletRequest);
            filterChain.doFilter(xssRequest, servletResponse);
        }

    }


    private boolean isRequestSkip(String path){
        return requestProperties.getSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    private boolean isXssSkip(String path){
        return xssProperties.getSkipUrl().stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    @Override
    public void destroy() {
    }
}
