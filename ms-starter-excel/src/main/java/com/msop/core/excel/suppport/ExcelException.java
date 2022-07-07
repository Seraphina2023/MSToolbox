package com.msop.core.excel.suppport;

/**
 * Excel异常
 *
 * @author ruozhuliufeng
 */
public class ExcelException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExcelException(String message){
        super(message);
    }
}
