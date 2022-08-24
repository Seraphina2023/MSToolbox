package com.core.core.sms.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 通知内容
 *
 * @author ruozhuliufeng
 */
@Data
@Accessors(chain = true)
public class SmsData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 构造器
     *
     * @param params 参数列表
     */
    public SmsData(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 变量key,用于从参数列表获取变量值
     */
    private String key;

    /**
     * 参数列表
     */
    private Map<String, String> params;
}
