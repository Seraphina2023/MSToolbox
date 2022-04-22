package com.msop.core.log.trace;


import com.msop.core.log.properties.TraceProperties;
import feign.RequestInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * feign拦截器，传递traceId
 *
 * @author ruozhuliufeng
 * 注解注释1：@Conditional：满足特定条件创建一个bean
 * 注解注释2：@ConditionalOnClass：满足当前类路径下有指定的类的条件下创建一个bean
 */
@Configuration
@ConditionalOnClass(value = {RequestInterceptor.class})
@AllArgsConstructor
public class FeignTraceConfig {
    private TraceProperties traceProperties;

    @Bean
    public RequestInterceptor feignTraceInterceptor() {
        return requestTemplate -> {
            if (traceProperties.getEnable()) {
                // 传递日志traceId
                String traceId = MDCTraceUtils.getTraceId();
                if (!StringUtils.isEmpty(traceId)) {
                    requestTemplate.header(MDCTraceUtils.TRACE_ID_HEADER, traceId);
                    requestTemplate.header(MDCTraceUtils.SPAN_ID_HEADER, MDCTraceUtils.getNextSpanId());
                }
            }
        };
    }
}
