package com.msop.core.oss.model;

import lombok.Data;

/**
 * 文件相关信息
 * @author ruozhuliufeng
 */
@Data
public class FileInfo {
    /**
     * 文件链接
     */
    private String link;
    /**
     * 文件名称
     */
    private String name;
    /**
     * 原始文件名
     */
    private String originalName;
}
