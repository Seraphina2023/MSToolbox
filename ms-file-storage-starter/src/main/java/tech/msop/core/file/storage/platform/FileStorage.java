package tech.msop.core.file.storage.platform;

import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 文件存储接口，对应各个平台
 */
public interface FileStorage extends AutoCloseable {
    /**
     * 获取平台
     *
     * @return 平台名称
     */
    String getPlatform();

    /**
     * 设置平台
     *
     * @param platform 平台名称
     */
    void setPlatform(String platform);

    /**
     * 保存文件
     *
     * @param fileInfo 文件信息
     * @param pre      文件预处理器
     */
    boolean save(FileInfo fileInfo, UploadPretreatment pre);


    /**
     * 删除文件
     *
     * @param fileInfo 文件信息
     */
    boolean delete(FileInfo fileInfo);

    /**
     * 文件是否存在
     *
     * @param fileInfo 文件信息
     */
    boolean exists(FileInfo fileInfo);

    /**
     * 下载文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    void download(FileInfo fileInfo, Consumer<InputStream> consumer);

    /**
     * 下载缩略图文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    void downloadTh(FileInfo fileInfo, Consumer<InputStream> consumer);

    /**
     * 释放相关资源
     */
    void close();

}
