package tech.msop.core.actuate.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Http Cache配置
 */
@ConfigurationProperties("ms.http.cache")
public class MsHttpCacheProperties {

    /**
     * Http-cache 的 spring cache名，默认为msHttpCache
     */
    @Getter
    @Setter
    private String cacheName = "msHttpCache";
    /**
     * 默认拦截 /**
     */
    @Getter
    private final List<String> includePatterns = new ArrayList<String>() {{
        add("/**");
    }};

    /**
     * 默认排除静态文件目录
     */
    @Getter
    private final List<String> excludePatterns = new ArrayList<>();
}
