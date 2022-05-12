package com.msop.core.secure.registry;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Secure API 放行设置
 *
 * @author ruozhuliufeng
 */
@Data
public class SecureRegistry {
    private boolean enabled = false;
    private final List<String> defaultExcludePatterns = new ArrayList<>();
    private final List<String> excludePatterns = new ArrayList<>();

    public SecureRegistry() {
        this.defaultExcludePatterns.add("/actuator/health/**");
        this.defaultExcludePatterns.add("/v2/api-docs/**");
        this.defaultExcludePatterns.add("/auth/**");
        this.defaultExcludePatterns.add("/token/**");
        this.defaultExcludePatterns.add("/log/**");
        this.defaultExcludePatterns.add("/user/user-info");
        this.defaultExcludePatterns.add("/user/user-info-by-id");
        this.defaultExcludePatterns.add("/menu/auth-routes");
        this.defaultExcludePatterns.add("/error/**");
        this.defaultExcludePatterns.add("/assets/**");
    }

    /**
     * 设置放行API
     */
    public SecureRegistry excludePathPatterns(String... patterns) {
        return excludePathPatterns(Arrays.asList(patterns));
    }

    /**
     * 设置放行API
     */
    public SecureRegistry excludePathPatterns(List<String> patterns) {
        this.excludePatterns.addAll(patterns);
        return this;
    }
}
