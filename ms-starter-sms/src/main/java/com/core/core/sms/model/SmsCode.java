package com.core.core.sms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 校验信息
 *
 * @author ruozhuliufeng
 */
@Data
@Accessors(chain = true)
public class SmsCode implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    private boolean success = Boolean.TRUE;

    /**
     * 变量phone,用于redis进行比对
     */
    private String phone;

    /**
     * 变量id,用于redis进行比对
     */
    private String id;

    /**
     * 变量值,用于redis进行比对
     */
    @JsonIgnore
    private String value;
}
