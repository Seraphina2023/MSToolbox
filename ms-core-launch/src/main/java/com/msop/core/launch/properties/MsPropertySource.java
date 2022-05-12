package com.msop.core.launch.properties;

import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 自定义资源文件读物，优先级最低
 *
 * @author ruozhuliufeng
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MsPropertySource {
    /**
     * Indicate the resource location(s) of the properties file to be loaded.
     * for example, {@code "classpath:/com/example/app.yml"}
     *
     * @return location(s)
     */
    String value();

    /**
     * load app-{activeProfile}.yml
     *
     * @return {boolean}
     */
    boolean loadActiveProfile() default true;

    /**
     * Get the order value of this resource.
     *
     * @return order
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
