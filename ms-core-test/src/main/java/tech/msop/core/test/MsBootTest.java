package tech.msop.core.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 简化测试
 *
 * @author ruozhuliufeng
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest
public @interface MsBootTest {
    /**
     * 服务名：appName
     *
     * @return appName
     */
    @AliasFor("appName")
    String value() default "ms-test";

    /**
     * 服务名：appName
     *
     * @return appName
     */
    @AliasFor("value")
    String appName() default "ms-test";

    /**
     * profile
     *
     * @return profile
     */
    @AliasFor("profile")
    String profile() default "dev";

    /**
     * 启用 ServiceLoader 加载 launcherService
     *
     * @return 是否启用
     */
    boolean enableLoader() default false;
}
