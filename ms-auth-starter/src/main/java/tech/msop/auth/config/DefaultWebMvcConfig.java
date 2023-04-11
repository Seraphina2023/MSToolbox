package tech.msop.auth.config;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 默认的Spring MVC 拦截器
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
public class DefaultWebMvcConfig implements WebMvcConfigurer {

    /**
     * Token 参数解析
     * @param resolvers 解析类
     */
    @Override
    public void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
