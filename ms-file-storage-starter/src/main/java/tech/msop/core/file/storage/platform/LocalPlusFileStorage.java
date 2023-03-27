package tech.msop.core.file.storage.platform;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.exception.FileStorageException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 本地文件存储
 */
@Getter
@Setter
public class LocalPlusFileStorage implements FileStorage {
    /**
     * 基础路径
     */
    private String basePath;
    /**
     * 本地存储路径
     */
    private String storagePath;
    /**
     * 存储平台
     */
    private String platform;
    /**
     * 访问域名
     */
    private String domain;

    /**
     * 获取本地绝对路径
     *
     * @param path 路径
     * @return 绝对路径
     */
    public String getAbsolutePath(String path) {
        return storagePath + path;
    }

    /**
     * 保存文件
     *
     * @param fileInfo 文件信息
     * @param pre      文件预处理器
     */
    @Override
    public boolean save(FileInfo fileInfo, UploadPretreatment pre) {
        String newFileKey = basePath + fileInfo.getPath() + fileInfo.getFilename();
        fileInfo.setBasePath(basePath);
        fileInfo.setUrl(domain + newFileKey);

        try {
            File newFile = FileUtil.touch(getAbsolutePath(newFileKey));
            pre.getFileWrapper().transferTo(newFile);
            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(newThFileKey);
                FileUtil.writeBytes(thumbnailBytes, getAbsolutePath(newThFileKey));
            }
            return true;
        } catch (IOException e) {
            FileUtil.del(getAbsolutePath(newFileKey));
            throw new FileStorageException("文件上传失败！platform:" + platform + ",filename:" + fileInfo.getOriginalFilename(), e);
        }
    }

    /**
     * 删除文件
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean delete(FileInfo fileInfo) {
        if (fileInfo.getThFilename() != null) {
            // 删除缩略图
            FileUtil.del(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename()));
        }
        return FileUtil.del(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()));
    }

    /**
     * 文件是否存在
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean exists(FileInfo fileInfo) {
        return new File(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())).exists();
    }

    /**
     * 下载文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        try (InputStream in = FileUtil.getInputStream(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()))) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new FileStorageException("文件下载失败!fileInfo:" + fileInfo, e);
        }
    }

    /**
     * 下载缩略图文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    @Override
    public void downloadTh(FileInfo fileInfo, Consumer<InputStream> consumer) {
        if (StrUtil.isBlank(fileInfo.getThFilename())) {
            throw new FileStorageException("缩略图文件下载失败，文件不存在!fileInfo:" + fileInfo);
        }
        try (InputStream in = FileUtil.getInputStream(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename()))) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new FileStorageException("缩略图文件下载失败!fileInfo:" + fileInfo, e);
        }
    }

    /**
     * 释放相关资源
     */
    @Override
    public void close() {

    }
}
