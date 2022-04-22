package com.msop.core.common.exception;

import com.msop.core.common.model.CodeEnum;
import lombok.Getter;

public class SecureException extends RuntimeException {
    private static final long serialVersionUID = 6610083281801529147L;

    @Getter
    private final CodeEnum codeEnum;

    public SecureException(String message) {
        super(message);
        this.codeEnum = CodeEnum.UN_AUTHORIZED;
    }

    public SecureException(CodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.codeEnum = codeEnum;
    }

    public SecureException(CodeEnum codeEnum,Throwable cause){
        super(cause);
        this.codeEnum = codeEnum;
    }


    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
