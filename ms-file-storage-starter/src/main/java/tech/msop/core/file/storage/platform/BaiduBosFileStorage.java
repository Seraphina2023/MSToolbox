package tech.msop.core.file.storage.platform;

import cn.hutool.core.util.StrUtil;
import com.baidubce.Protocol;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.model.BosObject;
import com.baidubce.services.bos.model.ObjectMetadata;
import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.exception.FileStorageException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 百度云 BOS 存储
 */
@Getter
@Setter
public class BaiduBosFileStorage implements FileStorage {
    // 存储平台参数
    private String platform;
    private String accessKey;
    private String secretKey;
    private String region;
    private String endPoint;
    private String bucketName;
    private String domain;
    private String basePath;
    private BosClient client;

    public BosClient getClient() {
        if (client == null) {
            BosClientConfiguration config = new BosClientConfiguration();
            config.setCredentials(new DefaultBceCredentials(accessKey,secretKey));
            config.setEndpoint(endPoint);
            config.setProtocol(Protocol.HTTPS);
            client = new BosClient();
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
        BosClient client = getClient();
        try (InputStream in = pre.getFileWrapper().getInputStream()) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileInfo.getSize());
            metadata.setContentType(fileInfo.getContentType());
            client.putObject(bucketName, newFileKey, in, metadata);
            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                // 上传缩略图
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                ObjectMetadata thMetadata = new ObjectMetadata();
                thMetadata.setContentLength(thumbnailBytes.length);
                thMetadata.setContentType(fileInfo.getThContentType());
                client.putObject(bucketName, newThFileKey, new ByteArrayInputStream(thumbnailBytes), thMetadata);
            }
            return true;
        } catch (IOException e) {
            client.deleteObject(bucketName, newFileKey);
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
        BosClient client = getClient();
        if (fileInfo.getThFilename() != null) {
            // 删除缩略图
            client.deleteObject(bucketName, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename());
        }
        client.deleteObject(bucketName, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
        return true;
    }

    /**
     * 文件是否存在
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean exists(FileInfo fileInfo) {
        return getClient().doesObjectExist(bucketName, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
    }

    /**
     * 下载文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        BosObject object = getClient().getObject(bucketName, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
        try (InputStream in = object.getObjectContent()) {
            consumer.accept(in);
        } catch (IOException e) {
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
        BosObject object = getClient().getObject(bucketName, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename());
        try (InputStream in = object.getObjectContent()) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new FileStorageException("缩略图下载失败!fileInfo:" + fileInfo);
        }
    }

    /**
     * 释放相关资源
     */
    @Override
    public void close() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }
}
