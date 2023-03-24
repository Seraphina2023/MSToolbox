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
public class LocalFileStorage implements FileStorage {

    private String basePath;
    private String platform;
    private String domain;

    /**
     * 保存文件
     *
     * @param fileInfo 文件信息
     * @param pre      文件预处理器
     */
    @Override
    public boolean save(FileInfo fileInfo, UploadPretreatment pre) {
        String path = fileInfo.getPath();
        File newFile = FileUtil.touch(basePath + path, fileInfo.getFilename());
        fileInfo.setBasePath(basePath);
        fileInfo.setUrl(domain + path + fileInfo.getFilename());

        try {
            pre.getFileWrapper().transferTo(newFile);
            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                fileInfo.setThUrl(domain + path + fileInfo.getThFilename());
                FileUtil.writeBytes(thumbnailBytes, basePath + path + fileInfo.getThFilename());
            }
            return true;
        } catch (IOException e) {
            FileUtil.del(newFile);
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
            FileUtil.del(new File(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename()));
        }
        return FileUtil.del(new File(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()));
    }

    /**
     * 文件是否存在
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean exists(FileInfo fileInfo) {
        return new File(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()).exists();
    }

    /**
     * 下载文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        try (InputStream in = FileUtil.getInputStream(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())) {
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
        try (InputStream in = FileUtil.getInputStream(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename())) {
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
