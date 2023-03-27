package tech.msop.core.file.storage.platform;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.sardine.impl.SardineException;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.PathUtil;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.exception.FileStorageException;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

/**
 * WebDav 存储
 */
@Getter
@Setter
public class WebDavFileStorage implements FileStorage {
    private String server;
    private String platform;
    private String user;
    private String password;
    private String domain;
    private String basePath;
    private String storagePath;
    private Sardine client;

    /**
     * 不支持单例模式运行，每次使用完需要销毁
     */
    public Sardine getClient() {
        if (client == null) {
            client = SardineFactory.begin(user, password);
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
        String path = basePath + fileInfo.getPath();
        String newFileKey = path + fileInfo.getFilename();
        fileInfo.setBasePath(basePath);
        fileInfo.setUrl(domain + newFileKey);
        Sardine client = getClient();
        try (InputStream in = pre.getFileWrapper().getInputStream()) {
            byte[] bytes = IoUtil.readBytes(in);
            createDirectory(client, getUrl(path));
            client.put(getUrl(newFileKey), bytes);
            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                String newThFileKey = path + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                client.put(getUrl(newThFileKey), thumbnailBytes);
            }
            return true;
        } catch (IOException | IORuntimeException e) {
            try {
                client.delete(getUrl(newFileKey));
            } catch (IOException ignored) {
            }
            throw new FileStorageException("文件上传失败! platform:" + platform + ",filename:" + fileInfo.getOriginalFilename(), e);
        }
    }

    /**
     * 删除文件
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean delete(FileInfo fileInfo) {
        Sardine client = getClient();
        try {
            if (fileInfo.getThFilename() != null) {
                // 删除缩略图
                try {
                    client.delete(getUrl(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename()));
                } catch (SardineException e) {
                    if (e.getStatusCode() != 404) {
                        throw e;
                    }
                }
            }
            try {
                client.delete(getUrl(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()));
            } catch (SardineException e) {
                if (e.getStatusCode() != 404) {
                    throw e;
                }
            }
            return true;
        } catch (IOException e) {
            throw new FileStorageException("文件删除失败! fileInfo：" + fileInfo, e);
        }
    }

    /**
     * 文件是否存在
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean exists(FileInfo fileInfo) {
        try {
            return getClient().exists(getUrl(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()));
        } catch (IOException e) {
            throw new FileStorageException("查询文件是否存在失败! fileInfo:" + fileInfo, e);
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
        try (InputStream in = getClient().get(getUrl(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()))) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new FileStorageException("文件下载失败! platform:" + fileInfo, e);
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
        try (InputStream in = getClient().get(getUrl(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename()))) {
            consumer.accept(in);
        } catch (IOException e) {
            throw new FileStorageException("缩略图文件下载失败! platform:" + fileInfo, e);
        }
    }

    /**
     * 释放相关资源
     */
    @SneakyThrows
    @Override
    public void close() {
        if (client != null) {
            client.shutdown();
            client = null;
        }
    }

    /**
     * 获取远程绝对路径
     *
     * @param path 路径
     * @return 远程绝对路径
     */
    public String getUrl(String path) {
        return PathUtil.join(server, storagePath + path);
    }

    /**
     * 递归创建目录
     *
     * @param client webdav 客户端
     * @param path   目录
     * @throws IOException 异常
     */
    public void createDirectory(Sardine client, String path) throws IOException {
        if (!client.exists(path)) {
            createDirectory(client, PathUtil.join(PathUtil.getParent(path), "/"));
            client.createDirectory(path);
        }
    }
}
