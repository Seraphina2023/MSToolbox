package com.msop.core.boot.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * 异常类型
 *
 * @author ruozhuliufeng
 */
@Getter
@RequiredArgsConstructor
public enum ErrorType {
    REQUEST("request"),
    ASYNC("async"),
    SCHEDULER("scheduler"),
    WEB_SOCKET("websocket"),
    OTHER("other");

    @JsonValue
    private final String type;

    @Nullable
    @JsonCreator
    public static ErrorType of(String value) {
        ErrorType[] values = ErrorType.values();
        for (ErrorType errorType : values) {
            if (errorType.type.equals(value)) {
                return errorType;
            }
        }
        return null;
    }
}
