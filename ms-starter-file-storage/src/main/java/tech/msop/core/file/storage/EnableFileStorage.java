package tech.msop.core.file.storage;

import java.lang.annotation.*;

/**
 * 启用文件存储，可以自动根据配置文件进行加载
 *
 * @author ruozhuliufeng
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface EnableFileStorage {
}
