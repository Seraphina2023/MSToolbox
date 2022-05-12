package com.msop.core.tool.request;

import com.msop.core.tool.propertis.MsRequestProperties;
import com.msop.core.tool.propertis.MsXssProperties;
import lombok.AllArgsConstructor;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * Request 全局过滤
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
public class MsRequestFilter implements Filter {

    private final MsRequestProperties requestProperties;
    private final MsXssProperties xssProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {
    }

    private boolean isRequestSkip(String path) {
        return requestProperties.getSkipUrl().stream().anyMatch(url -> antPathMatcher.match(url, path));
    }

    private boolean isXssSkip(String path) {
        return xssProperties.getSkipUrl().stream().anyMatch(url -> antPathMatcher.match(url, path));
    }
}
