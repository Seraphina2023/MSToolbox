package com.msop.core.tool.utils;

import com.msop.core.tool.support.FastStringWriter;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

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

    /**
     * 代理异常解包
     *
     * @param wrapped 包装过的异常
     * @return 解包后的异常
     */
    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;
        while (true) {
            if (unwrapped instanceof InvocationTargetException) {
                unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
            } else if (unwrapped instanceof UndeclaredThrowableException) {
                unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
            } else {
                return unwrapped;
            }
        }
    }

    /**
     * 将ErrorStack转化为String
     *
     * @param throwable 错误堆栈信息
     * @return String
     */
    public static String getStackTraceAsString(Throwable throwable) {
        FastStringWriter stringWriter = new FastStringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
