package com.core.core.sms.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

/**
 * 通知信息
 */
@Data
@Accessors(chain = true)
public class SmsInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 通知内容
     */
    private SmsData smsData;

    /**
     * 号码列表
     */
    private Collection<String> phones;
}
