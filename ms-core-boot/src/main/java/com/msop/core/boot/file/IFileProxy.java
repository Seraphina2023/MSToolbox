package com.msop.core.boot.file;

import java.io.File;

/**
 * 文件代理接口
 *
 * @author ruozhuliufeng
 */
public interface IFileProxy {

    /**
     * 返回路径 [物理路径] [虚拟路径]
     *
     * @param file 文件对象
     * @param dir  文件目录
     * @return 路径
     */
    String[] path(File file, String dir);

    /**
     * 文件重命名策略
     *
     * @param file 文件对象
     * @param path 文件路径
     * @return 文件名
     */
    File rename(File file, String path);

    /**
     * 图片压缩
     *
     * @param path 图片路径
     */
    void compress(String path);
}
