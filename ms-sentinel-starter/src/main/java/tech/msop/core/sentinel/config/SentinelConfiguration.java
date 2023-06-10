package tech.msop.core.sentinel.config;

import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import tech.msop.core.tool.jackson.JsonUtil;
import tech.msop.core.tool.model.Result;

import javax.servlet.http.HttpServletRequest;

/**
 * Sentinel 配置类
 *
 * @author ruozhuliufeng
 */
@AutoConfiguration
public class SentinelConfiguration {

    /**
     * 限流、熔断统一处理类
     */
    @AutoConfiguration
    @ConditionalOnClass(HttpServletRequest.class)
    public static class WebMvcHandler {
        @Bean
        public BlockExceptionHandler webmvcBlockExceptionHandler() {
            return (request, response, e) -> {
                response.setStatus(429);
                Result result = Result.failed(e.getMessage());
                response.getWriter().print(JsonUtil.toJson(result));
            };
        }
    }

    /**
     * 限流、熔断统一处理类
     */
    @AutoConfiguration
    @ConditionalOnClass(ServerResponse.class)
    public static class WebfluxHandler {
        @Bean
        public BlockRequestHandler webfluxBlockRequestHandler() {
            return (exchange, t) ->
                    ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(Result.failed(t.getMessage())));
        }
    }
}
