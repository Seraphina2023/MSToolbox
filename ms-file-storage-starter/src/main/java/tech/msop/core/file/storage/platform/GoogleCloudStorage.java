package tech.msop.core.file.storage.platform;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.ReadChannel;
import com.google.cloud.storage.*;
import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.exception.FileStorageException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Google 云盘存储策略
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
public class GoogleCloudStorage implements FileStorage {
    private String platform;

    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 证书路径，兼容Spring的ClassPath路径、文件路径、HTTP路径等
     */
    private String credentialsPath;
    /**
     * 存储桶
     */
    private String bucketName;
    /**
     * 访问域名
     */
    private String domain;
    /**
     * 基础路径
     */
    private String basePath;
    /**
     * 谷歌Client
     */
    private Storage client;

    public Storage getClient() {
        if (client == null) {
            ServiceAccountCredentials credentialsFromStream;
            try (InputStream in = URLUtil.url(credentialsPath).openStream()) {
                credentialsFromStream = ServiceAccountCredentials.fromStream(in);
            } catch (IOException e) {
                throw new FileStorageException("Google Cloud Platform 授权key文件获取失败！credentialsPath:" + credentialsPath, e);
            }
            List<String> scopes = Collections.singletonList("https://www.googleapis.com/auth/cloud-platform");
            ServiceAccountCredentials credentials = credentialsFromStream.toBuilder().setScopes(scopes).build();
            StorageOptions storageOptions = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentials).build();
            client = storageOptions.getService();
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
        Storage client = getClient();
        BlobId blobId = BlobId.of(bucketName, newFileKey);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(fileInfo.getContentType()).build();
        try (InputStream in = pre.getFileWrapper().getInputStream()) {
            // 上传文件
            client.createFrom(blobInfo, in);
            // 缩略图
            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                BlobId thBlobId = BlobId.of(bucketName, newThFileKey);
                BlobInfo thBlobInfo = BlobInfo.newBuilder(thBlobId).setContentType(fileInfo.getThContentType()).build();
                client.createFrom(thBlobInfo, new ByteArrayInputStream(thumbnailBytes));
            }
            return true;
        } catch (IOException e) {
            checkAndDelete(newFileKey);
            throw new FileStorageException("文件上传失败!platform:" + platform + ",filename:" + fileInfo.getOriginalFilename(), e);
        }
    }

    /**
     * 检查并删除对象
     * <a href="https://github.com/googleapis/java-storage/blob/main/samples/snippets/src/main/java/com/example/storage/object/DeleteObject.java">Source Example</a>
     *
     * @param fileKey 对象key
     */
    private void checkAndDelete(String fileKey) {
        Storage client = getClient();
        Blob blob = client.get(bucketName, fileKey);
        if (blob != null) {
            Storage.BlobSourceOption precondition = Storage.BlobSourceOption.generationMatch(blob.getGeneration());
            client.delete(bucketName, fileKey, precondition);
        }
    }

    /**
     * 删除文件
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean delete(FileInfo fileInfo) {
        // 删除缩略图
        if (fileInfo.getThFilename() != null) {
            checkAndDelete(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename());
        }
        checkAndDelete(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
        return true;
    }

    /**
     * 文件是否存在
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean exists(FileInfo fileInfo) {
        Storage client = getClient();
        BlobId blobId = BlobId.of(bucketName, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
        return client.get(blobId) != null;
    }

    /**
     * 下载文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        Storage client = getClient();
        BlobId blobId = BlobId.of(bucketName, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
        ReadChannel readChannel = client.reader(blobId);
        InputStream in = Channels.newInputStream(readChannel);
        consumer.accept(in);
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
            throw new FileStorageException("缩略图文件下载失败,文件不存在!fileInfo:" + fileInfo);
        }
        Storage client = getClient();
        BlobId blobId = BlobId.of(bucketName, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename());
        ReadChannel readChannel = client.reader(blobId);
        InputStream in = Channels.newInputStream(readChannel);
        consumer.accept(in);
    }

    /**
     * 释放相关资源
     */
    @Override
    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                throw new FileStorageException(e);
            }
            client = null;
        }
    }
}
