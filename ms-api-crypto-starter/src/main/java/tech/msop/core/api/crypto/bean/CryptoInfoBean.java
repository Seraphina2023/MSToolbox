package tech.msop.core.api.crypto.bean;

import tech.msop.core.api.crypto.enums.CryptoType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 加密注解信息
 *
 * @author ruozhuliufeng
 */
@Getter
@RequiredArgsConstructor
public class CryptoInfoBean {
    /**
     * 加密类型
     */
    private final CryptoType type;
    /**
     * 私钥
     */
    private final String secretKey;
}
