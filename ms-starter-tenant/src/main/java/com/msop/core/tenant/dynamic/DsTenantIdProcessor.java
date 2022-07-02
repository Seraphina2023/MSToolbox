package com.msop.core.tenant.dynamic;

import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.msop.core.secure.utils.AuthUtil;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 租户动态数据源解析器
 *
 * @author ruozhuliufeng
 */
public class DsTenantIdProcessor extends DsProcessor {
    public static final String TENANT_ID_KEY = "#token.tenantId";
    @Override
    public boolean matches(String key) {
        return key.equals(TENANT_ID_KEY);
    }

    @Override
    public String doDetermineDatasource(MethodInvocation methodInvocation, String s) {
        return AuthUtil.getTenantId();
    }
}
