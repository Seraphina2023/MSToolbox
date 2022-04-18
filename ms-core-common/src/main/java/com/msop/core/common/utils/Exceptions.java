package com.msop.core.common.utils;

import java.lang.reflect.InvocationTargetException;

/**
 * 异常处理工具类
 *
 * @author ruozhuliufeng
 */
public class Exceptions {


    /**
     * 将ChcckedException转换为UncheckedException
     *
     * @param throwable 异常信息
     * @return 运行时异常
     */
    public static RuntimeException unchecked(Throwable throwable) {
        if (throwable instanceof IllegalAccessException
                || throwable instanceof IllegalArgumentException
                || throwable instanceof NoSuchMethodException) {
            return new IllegalArgumentException(throwable);
        } else if (throwable instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) throwable).getTargetException());
        } else if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        } else {
            return new RuntimeException(throwable);
        }
    }
}
