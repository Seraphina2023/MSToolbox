package tech.msop.core.file.storage.platform;

import cn.hutool.core.util.StrUtil;
import io.minio.*;
import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.exception.FileStorageException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * MinIO 存储
 */
@Getter
@Setter
public class MinIOFileStorage implements FileStorage {
    // 存储平台参数
    private String platform;
    private String accessKey;
    private String secretKey;
    private String region;
    private String endPoint;
    private String bucketName;
    private String domain;
    private String basePath;
    private MinioClient client;

    public MinioClient getClient() {
        if (client == null) {
            client = new MinioClient.Builder().credentials(accessKey, secretKey)
                    .endpoint(endPoint).build();
        }
        return client;
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
        MinioClient client = getClient();
        try (InputStream in = pre.getFileWrapper().getInputStream()) {
            client.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(newFileKey)
                    .stream(in, pre.getFileWrapper().getSize(), -1)
                    .contentType(fileInfo.getContentType())
                    .build());
            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                // 上传缩略图
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                client.putObject(PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newThFileKey)
                        .stream(new ByteArrayInputStream(thumbnailBytes), pre.getFileWrapper().getSize(), -1)
                        .contentType(fileInfo.getThContentType())
                        .build());
            }
            return true;
        } catch (Exception e) {
            try {
                client.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(newFileKey)
                        .build());
            } catch (Exception ignored) {
            }
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
        MinioClient client = getClient();
        try {
            if (fileInfo.getThFilename() != null) {
                // 删除缩略图
                client.removeObject(RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename())
                        .build());
            }
            client.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())
                    .build());
            return true;
        } catch (Exception e) {
            throw new FileStorageException("文件删除失败!fileInfo:" + fileInfo, e);
        }
    }

    /**
     * 文件是否存在
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean exists(FileInfo fileInfo) {
        MinioClient client = getClient();
        try {
            StatObjectResponse stat = client.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())
                    .build());
            return stat != null && stat.lastModified() != null;
        } catch (Exception e) {
            throw new SecurityException("查询文件是否存在失败!fileInfo:" + fileInfo, e);
        }
    }

    /**
     * 下载文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        MinioClient client = getClient();
        try (InputStream in = client.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()).build())) {
            consumer.accept(in);
        } catch (Exception e) {
            throw new FileStorageException("文件下载失败!fileInfo:" + fileInfo);
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
            throw new FileStorageException("缩略图文件下载失败，文件不存在！fileInfo:" + fileInfo);
        }
        MinioClient client = getClient();
        try (InputStream in = client.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename()).build())) {
            consumer.accept(in);
        } catch (Exception e) {
            throw new FileStorageException("缩略图下载失败!fileInfo:" + fileInfo);
        }
    }

    /**
     * 释放相关资源
     */
    @Override
    public void close() {
        if (client != null) {
            client = null;
        }
    }
}
