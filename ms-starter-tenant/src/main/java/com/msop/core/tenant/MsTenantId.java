package com.msop.core.tenant;

import com.msop.core.tool.utils.RandomType;
import com.msop.core.tool.utils.StringUtil;

/**
 * Ms 租户ID生成器
 *
 * @author ruozhuliufeng
 */
public class MsTenantId implements TenantId {
    /**
     * 生成自定义租户ID
     *
     * @return 租户ID
     */
    @Override
    public String generate() {
        return StringUtil.random(6, RandomType.INT);
    }
}
