package com.msop.core.tenant.dynamic;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.msop.core.secure.utils.AuthUtil;
import com.msop.core.tenant.exception.TenantDataSourceException;
import com.msop.core.tool.utils.StringUtil;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 租户数据源全局拦截器
 */
public class TenantDataSourceGlobalInterceptor implements MethodInterceptor {
    @Setter
    private TenantDataSourceHolder holder;
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        String tenantId = AuthUtil.getTenantId();
        try {
            if (StringUtil.isNotBlank(tenantId)) {
                holder.handleDataSource(tenantId);
                DynamicDataSourceContextHolder.push(tenantId);
            }
            return methodInvocation.proceed();
        } catch (Exception exception) {
            throw new TenantDataSourceException(exception.getMessage());
        } finally {
            if (StringUtil.isNotBlank(tenantId)) {
                DynamicDataSourceContextHolder.poll();
            }
        }
    }
}
