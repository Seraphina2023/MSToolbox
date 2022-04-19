package com.msop.core.oss.template;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.msop.core.oss.model.FileInfo;
import com.msop.core.oss.propertis.OssProperties;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

/**
 * FastDFS配置
 *
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 16:31
 **/

@AllArgsConstructor
public class FdfsTemplate {
    private OssProperties ossProperties;
    private FastFileStorageClient storageClient;

    @SneakyThrows
    public FileInfo upload(String objectName, InputStream inputStream) {
        return upload(objectName, inputStream, inputStream.available());
    }

    @SneakyThrows
    public FileInfo upload(MultipartFile file) {
        return upload(file.getOriginalFilename(), file.getInputStream());
    }

    /**
     * 上传对象
     *
     * @param objectName  对象名
     * @param inputStream 输入流
     * @param size        大小
     * @return ObjectInfo
     */
    private FileInfo upload(String objectName, InputStream inputStream, long size) {
        StorePath storePath = storageClient.uploadFile(inputStream, size, FilenameUtils.getExtension(objectName), null);
        FileInfo objectInfo = new FileInfo();
        objectInfo.setName(storePath.getFullPath());
        objectInfo.setLink("http://" + ossProperties.getEndpoint());
        return objectInfo;
    }

    /**
     * 删除对象
     *
     * @param objectPath 对象路径
     */
    public void delete(String objectPath) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            storageClient.deleteFile(storePath.getGroup(), storePath.getPath());
        }
    }

    /**
     * 下载对象
     *
     * @param objectPath 对象路径
     * @param callback   回调
     */
    public <T> T download(String objectPath, DownloadCallback<T> callback) {
        if (!StringUtils.isEmpty(objectPath)) {
            StorePath storePath = StorePath.parseFromUrl(objectPath);
            return storageClient.downloadFile(storePath.getGroup(), storePath.getPath(), callback);
        }
        return null;
    }
}
