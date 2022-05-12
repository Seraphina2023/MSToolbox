package com.msop.core.secure.model;

import lombok.Data;

/**
 * TokenInfo
 *
 * @author ruozhuliufeng
 */
@Data
public class TokenInfo {

    /**
     * 令牌值
     */
    private String token;

    /**
     * 过期秒数
     */
    private int expire;

}
