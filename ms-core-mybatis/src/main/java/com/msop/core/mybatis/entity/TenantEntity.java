package com.msop.core.mybatis.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TenantEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /**
     * 租户ID
     */
    private String tenantId;
}
