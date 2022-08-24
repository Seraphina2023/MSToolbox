package com.core.core.sms.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SMS 枚举类
 *
 * @author ruozhuliufeng
 */
@Getter
@AllArgsConstructor
public enum SmsEnum {

    /**
     * yunpian
     */
    YUNPIAN("yunpian", 1),

    /**
     * qiniu
     */
    QINIU("qiniu", 2),

    /**
     * ali
     */
    ALI("ali", 3),

    /**
     * tencent
     */
    TENCENT("tencent", 4),
    ;

    /**
     * 名称
     */
    final String name;
    /**
     * 类型
     */
    final int category;

    /**
     * 匹配枚举值
     *
     * @param name 名称
     * @return OssEnum
     */
    public static SmsEnum of(String name) {
        if (name == null) {
            return null;
        }
        SmsEnum[] values = SmsEnum.values();
        for (SmsEnum smsEnum : values) {
            if (smsEnum.name.equals(name)) {
                return smsEnum;
            }
        }
        return null;
    }
}
