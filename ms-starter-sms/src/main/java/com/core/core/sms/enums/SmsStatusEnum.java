package com.core.core.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SMS 状态枚举
 *
 * @author ruozhuliufeng
 */
@Getter
@AllArgsConstructor
public enum SmsStatusEnum {
    /**
     * 关闭
     */
    DISABLE(1),
    /**
     * 启用
     */
    ENABLE(2),
    ;

    /**
     * 类型编号
     */
    final int num;
}
