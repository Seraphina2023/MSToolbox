package tech.msop.core.api.crypto.annotation.crypto;

import tech.msop.core.api.crypto.annotation.decrypt.ApiDecrypt;
import tech.msop.core.api.crypto.annotation.encrypt.ApiEncrypt;
import tech.msop.core.api.crypto.enums.CryptoType;

import java.lang.annotation.*;

/**
 * <p>DES加密解密含有{@link org.springframework.web.bind.annotation.RequestBody}注解的参数请求数据</p>
 *
 * @author ruozhuilufeng
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ApiEncrypt(CryptoType.DES)
@ApiDecrypt(CryptoType.DES)
public @interface ApiCryptoDes {
}
