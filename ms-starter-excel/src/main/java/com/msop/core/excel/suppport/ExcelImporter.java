package com.msop.core.excel.suppport;

import java.util.List;

/**
 * Excel导入接口
 *
 * @param <T> 泛型
 * @author ruozhuliufeng
 */
public interface ExcelImporter<T> {
    /**
     * 导入数据逻辑
     *
     * @param data 数据
     */
    void save(List<T> data);
}
