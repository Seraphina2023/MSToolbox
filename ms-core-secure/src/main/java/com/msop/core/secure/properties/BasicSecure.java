package com.msop.core.secure.properties;

import com.msop.core.secure.provider.HttpMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 基础授权规则
 *
 * @author ruozhuliufeng
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicSecure {
    /**
     * 请求方法
     */
    private HttpMethod method;
    /**
     * 请求路径
     */
    private String pattern;
    /**
     * 客户端ID
     */
    private String username;
    /**
     * 客户端密钥
     */
    private String password;
}
