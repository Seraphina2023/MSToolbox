package tech.msop.core.tool.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.http.converter.*;
import tech.msop.core.tool.jackson.MappingApiJackson2HttpMessageConverter;
import tech.msop.core.tool.jackson.MsJacksonProperties;
import tech.msop.core.tool.utils.Charsets;
import tech.msop.core.tool.utils.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 消息配置类
 *
 * @author ruozhuliufeng
 */
@AutoConfiguration
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MessageConfiguration implements WebMvcConfigurer {
    private final ObjectMapper objectMapper;
    private final MsJacksonProperties properties;

    /**
     * 使用JACKSON 作为JSON MessageConverter
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(x -> x instanceof StringHttpMessageConverter || x instanceof AbstractJackson2HttpMessageConverter);
        converters.add(new StringHttpMessageConverter(Charsets.UTF_8));
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        converters.add(new ResourceRegionHttpMessageConverter());
        converters.add(new MappingApiJackson2HttpMessageConverter(objectMapper, properties));
    }

    /**
     * 日期格式化
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new DateFormatter(DateUtil.PATTERN_DATE));
        registry.addFormatter(new DateFormatter(DateUtil.PATTERN_DATETIME));
    }
}
