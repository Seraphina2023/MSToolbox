package com.msop.core.tenant;

import org.springframework.core.NamedThreadLocal;

/**
 * 租户线程处理
 *
 * @author ruozhuliufeng
 */
public class MsTenantHolder {

    public static final ThreadLocal<Boolean> TENANT_KEY_HOLDER = new NamedThreadLocal<Boolean>("ms-tenant"){
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    public static void setIgnore(Boolean tenant) {
        TENANT_KEY_HOLDER.set(tenant);
    }

    public static Boolean isIgnore() {
        return TENANT_KEY_HOLDER.get();
    }

    public static void clear() {
        TENANT_KEY_HOLDER.remove();
    }
}
