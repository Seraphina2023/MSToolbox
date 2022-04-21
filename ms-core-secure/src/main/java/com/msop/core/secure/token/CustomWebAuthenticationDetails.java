package com.msop.core.secure.token;

import lombok.Getter;

import java.io.Serializable;

/**
 * 表单登录的认证信息对象
 *
 * @author ruozhuliufeng
 **/
@Getter
public class CustomWebAuthenticationDetails implements Serializable {
    private static final long serialVersionUID = -1;

    private final String accountType;
    private final String remoteAddress;
    private final String sessionId;

    public CustomWebAuthenticationDetails(String remoteAddress, String sessionId, String accountType) {
        this.remoteAddress = remoteAddress;
        this.sessionId = sessionId;
        this.accountType = accountType;
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(";accountType: ")
                .append(this.getAccountType());
        return sb.toString();
    }
}
