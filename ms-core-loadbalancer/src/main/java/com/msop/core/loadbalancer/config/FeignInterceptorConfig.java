package com.msop.core.loadbalancer.config;

import com.msop.core.common.constant.MsConstant;
import com.msop.core.common.context.TenantContextHolder;
import com.msop.core.common.utils.Func;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

/**
 * feign拦截器，只包含基础数据
 *
 * @author ruozhuliufeng
 */
public class FeignInterceptorConfig {

    /**
     * 使用feign client访问别的微服务时，将上游传过来的client等信息放入header，传递给下一个微服务
     *
     * @return
     */
    @Bean
    public RequestInterceptor baseFeignInterceptor() {
        return template -> {
            // 传递client
            String tenant = TenantContextHolder.getTenant();
            if (Func.isNotEmpty(tenant)) {
                template.header(MsConstant.TENANT_HEADER, tenant);
            }
        };
    }
}
