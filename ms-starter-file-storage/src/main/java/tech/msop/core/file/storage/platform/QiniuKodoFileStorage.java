package tech.msop.core.file.storage.platform;

import cn.hutool.core.util.StrUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.exception.FileStorageException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.Consumer;

/**
 * 七牛云 Kodo 存储
 */
@Getter
@Setter
public class QiniuKodoFileStorage implements FileStorage {
    // 存储平台参数
    private String platform;
    private String accessKey;
    private String secretKey;
    private Region region;
    private String endPoint;
    private String bucketName;
    private String domain;
    private String basePath;
    private QiniuKodoClient client;

    public QiniuKodoClient getClient() {
        if (client == null) {
            client = new QiniuKodoClient(accessKey, secretKey);
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
        try (InputStream in = pre.getFileWrapper().getInputStream()) {
            QiniuKodoClient client = getClient();
            UploadManager uploadManager = client.getUploadManager();
            String token = client.getAuth().uploadToken(bucketName);
            uploadManager.put(in, newFileKey, token, null, fileInfo.getContentType());

            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                // 上传缩略图
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                uploadManager.put(new ByteArrayInputStream(thumbnailBytes), newThFileKey, token, null, fileInfo.getThContentType());
            }
            return true;
        } catch (IOException e) {
            try {
                client.getBucketManager().delete(bucketName, newFileKey);
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
        BucketManager manager = getClient().getBucketManager();
        try {
            if (fileInfo.getThFilename() != null) {
                // 删除缩略图
                delete(manager, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename());
            }
            delete(manager, fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename());
        }catch (QiniuException e){
            throw new FileStorageException("删除文件失败!"+e.code()+", "+e.response.toString(),e);
        }
        return true;
    }

    public void delete(BucketManager manager,String filename) throws QiniuException{
        try{
            manager.delete(bucketName,filename);
        }catch (QiniuException e){
            if (!(e.response != null && e.response.statusCode == 612)){
                throw e;
            }
        }
    }

    /**
     * 文件是否存在
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean exists(FileInfo fileInfo) {
        BucketManager manager = getClient().getBucketManager();
        try{
            com.qiniu.storage.model.FileInfo stat = manager.stat(bucketName,fileInfo.getBasePath()+fileInfo.getPath()+fileInfo.getFilename());
            if (stat != null && stat.md5 != null){
                return true;
            }
        }catch (QiniuException e){
            throw new FileStorageException("查询文件是否存在失败!"+e.code()+", "+e.response.toString(),e);
        }
        return false;
    }

    /**
     * 下载文件
     *
     * @param fileInfo 文件信息
     * @param consumer 处理器
     */
    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        String url = getClient().getAuth().privateDownloadUrl(fileInfo.getUrl());
        try (InputStream in = new URL(url).openStream()) {
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
        String url = getClient().getAuth().privateDownloadUrl(fileInfo.getThUrl());
        try (InputStream in = new URL(url).openStream()) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new FileStorageException("缩略图文件下载失败!fileInfo:" + fileInfo);
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


    @Getter
    @Setter
    public static class QiniuKodoClient {
        private String accessKey;
        private String secretKey;
        private Auth auth;
        private BucketManager bucketManager;
        private UploadManager uploadManager;

        public QiniuKodoClient(String accessKey, String secretKey) {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
        }

        public Auth getAuth() {
            if (auth == null) {
                auth = Auth.create(accessKey, secretKey);
            }
            return auth;
        }

        public BucketManager getBucketManager() {
            if (bucketManager == null) {
                bucketManager = new BucketManager(getAuth(), new Configuration(Region.autoRegion()));
            }
            return bucketManager;
        }

        public UploadManager getUploadManager() {
            if (uploadManager == null) {
                uploadManager = new UploadManager(new Configuration(Region.autoRegion()));
            }
            return uploadManager;
        }
    }
}
