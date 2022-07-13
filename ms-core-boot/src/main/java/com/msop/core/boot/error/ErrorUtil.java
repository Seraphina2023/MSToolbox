package com.msop.core.boot.error;

import com.msop.core.log.model.AuditErrorLog;
import com.msop.core.tool.utils.DateUtil;
import com.msop.core.tool.utils.Exceptions;
import com.msop.core.tool.utils.ObjectUtil;

/**
 * 异常工具类
 *
 * @author ruozhuliufeng
 */
public class ErrorUtil {

    /**
     * 初始化异常信息
     * @param error  异常
     * @param event 异常事件封装
     */
    public static void initErrorInfo(Throwable error, AuditErrorLog event){
        // 堆栈信息
        event.setStackTrace(Exceptions.getStackTraceAsString(error));
        event.setExceptionName(error.getClass().getName());
        event.setMessage(error.getMessage());
        event.setCreateTime(DateUtil.now());
        StackTraceElement[] stackTrace = error.getStackTrace();
        if(ObjectUtil.isNotEmpty(stackTrace)){
            // 报错的类信息
            StackTraceElement element = stackTrace[0];
            event.setMethodClass(element.getClassName());
            event.setFileName(element.getFileName());
            event.setMethodName(element.getMethodName());
            event.setLineNumber(element.getLineNumber());
        }

    }
}
