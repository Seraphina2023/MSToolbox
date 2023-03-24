package tech.msop.core.file.storage.platform;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.exception.FileStorageException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * FTP 存储策略
 */
@Getter
@Setter
public class FtpFileStorage implements FileStorage {
    /**
     * 主机
     */
    private String host;
    /**
     * 端口，默认 21
     */
    private int port;
    /**
     * 用户名，默认 anonymous(匿名)
     */
    private String user;
    /**
     * 密码，默认空
     */
    private String password;
    /**
     * 编码 默认UTF-8
     */
    private Charset charset;
    /**
     * 连接超时时长，单位毫秒，默认10秒 {@link org.apache.commons.net.SocketClient#setConnectTimeout(int)}
     */
    private long connectionTimeout;
    /**
     * Socket连接超时时长，单位毫秒，默认10秒 {@link org.apache.commons.net.SocketClient#setSoTimeout(int)}
     */
    private long soTimeout;
    /**
     * 设置服务器语言，默认空，{@link org.apache.commons.net.ftp.FTPClientConfig#setServerLanguageCode(String)}
     */
    private String serverLanguageCode;
    /**
     * 服务器标识，默认空，{@link org.apache.commons.net.ftp.FTPClientConfig#FTPClientConfig(String)}
     * 例如：org.apache.commons.net.ftp.FTPClientConfig.SYST_NT
     */
    private String systemKey;
    /**
     * 是否主动模式 默认为被动
     */
    private Boolean isActive;
    /**
     * 访问域名
     */
    private String domain;
    /**
     * 启用存储
     */
    private Boolean enableStorage;
    /**
     * 存储平台
     */
    private String platform;
    /**
     * 基础路径
     */
    private String basePath;
    /**
     * 存储路径，上传的文件都会存储在这个路径下，注意以"/"结尾
     */
    private String storagePath;

    public Ftp getClient() {
        FtpConfig config = FtpConfig.create()
                .setHost(host)
                .setPort(port)
                .setUser(user)
                .setPassword(password)
                .setCharset(charset)
                .setConnectionTimeout(connectionTimeout)
                .setSoTimeout(soTimeout)
                .setServerLanguageCode(serverLanguageCode)
                .setSystemKey(systemKey);
        return new Ftp(config, isActive ? FtpMode.Active : FtpMode.Passive);
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
        Ftp client = getClient();
        try (InputStream in = pre.getFileWrapper().getInputStream()) {
            client.upload(getAbsolutePath(basePath + fileInfo.getPath()), fileInfo.getFilename(), in);
            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                client.upload(getAbsolutePath(basePath + fileInfo.getPath()), fileInfo.getThFilename(), new ByteArrayInputStream(thumbnailBytes));
            }
            return true;
        } catch (IOException | IORuntimeException e) {
            try {
                client.delFile(getAbsolutePath(newFileKey));
            } catch (IORuntimeException ignored) {
            }
            throw new FileStorageException("文件上传失败!platform:" + platform + ",filename: " + fileInfo.getOriginalFilename(), e);
        } finally {
            IoUtil.close(client);
        }
    }

    /**
     * 删除文件
     *
     * @param fileInfo 文件信息
     */
    @Override
    public boolean delete(FileInfo fileInfo) {
        try (Ftp client = getClient()) {
            if (fileInfo.getThFilename() != null) {
                // 删除缩略图
                client.delFile(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename()));
            }
            client.delFile(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()));
            return true;
        } catch (IOException | IORuntimeException e) {
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
        try (Ftp client = getClient()) {
            return client.existFile(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename()));
        } catch (IOException | IORuntimeException e) {
            throw new FileStorageException("查询文件是否存在失败!fileInfo:" + fileInfo, e);
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
        try (Ftp client = getClient()) {
            client.cd(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath()));
            try (InputStream in = client.getClient().retrieveFileStream(fileInfo.getFilename())) {
                if (in == null) {
                    throw new FileStorageException("文件下载失败,文件不存在! fileInfo:" + fileInfo);
                }
                consumer.accept(in);
            }
        } catch (IOException | IORuntimeException e) {
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
            throw new FileStorageException("缩略图文件下载失败,文件不存在! fileInfo:" + fileInfo);
        }
        try (Ftp client = getClient()) {
            client.cd(getAbsolutePath(fileInfo.getBasePath() + fileInfo.getPath()));
            try (InputStream in = client.getClient().retrieveFileStream(fileInfo.getThFilename())) {
                if (in == null) {
                    throw new FileStorageException("缩略图文件下载失败,文件不存在! fileInfo:" + fileInfo);
                }
                consumer.accept(in);
            }
        } catch (IOException | IORuntimeException e) {
            throw new FileStorageException("缩略图文件下载失败!fileInfo:" + fileInfo, e);
        }
    }

    /**
     * 释放相关资源
     */
    @Override
    public void close() {

    }

    public String getAbsolutePath(String path) {
        return storagePath + path;
    }
}
