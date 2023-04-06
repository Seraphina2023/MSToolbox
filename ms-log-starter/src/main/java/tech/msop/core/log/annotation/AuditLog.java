package tech.msop.core.log.annotation;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author ruozhuliufeng
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuditLog {
    /**
     * 操作信息
     *
     * @return 操作信息
     */
    String value() default "日志记录";
}
