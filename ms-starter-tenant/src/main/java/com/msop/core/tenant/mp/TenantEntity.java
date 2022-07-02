package com.msop.core.tenant.mp;

import com.msop.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户基础实体
 *
 * @author ruozhuliufeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantEntity extends BaseEntity {

    /**
     * 租户ID
     */
    private String tenantId;
}
