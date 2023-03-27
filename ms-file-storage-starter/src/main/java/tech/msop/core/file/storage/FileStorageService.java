package tech.msop.core.file.storage;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.multipart.MultipartFile;
import tech.msop.core.file.storage.aspect.DeleteAspectChain;
import tech.msop.core.file.storage.aspect.ExistsAspectChain;
import tech.msop.core.file.storage.aspect.FileStorageAspect;
import tech.msop.core.file.storage.aspect.UploadAspectChain;
import tech.msop.core.file.storage.exception.FileStorageException;
import tech.msop.core.file.storage.platform.FileStorage;
import tech.msop.core.file.storage.properties.MsFileStorageProperties;
import tech.msop.core.file.storage.recorder.FileRecorder;
import tech.msop.core.file.storage.tika.TikaFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

/**
 * 用来处理文件存储，对接多个平台
 */
@Slf4j
@Getter
@Setter
public class FileStorageService implements DisposableBean {
    private FileStorageService self;
    private FileRecorder fileRecorder;
    private CopyOnWriteArrayList<FileStorage> fileStorageList;
    private MsFileStorageProperties properties;
    private CopyOnWriteArrayList<FileStorageAspect> aspectList;
    private TikaFactory tikaFactory;

    /**
     * 获取默认的存储平台
     *
     * @return FileStorage
     */
    public FileStorage getFileStorage() {
        return getFileStorage(properties.getDefaultPlatform());
    }

    /**
     * 获取对应的存储平台
     *
     * @param platform 平台标识
     * @return FileStorage
     */
    public FileStorage getFileStorage(String platform) {
        for (FileStorage fileStorage : fileStorageList) {
            if (fileStorage.getPlatform().equals(platform)) {
                return fileStorage;
            }
        }
        return null;
    }

    /**
     * 根据文件信息获取对应的存储平台，若存储平台不存在，则抛出异常
     *
     * @param fileInfo 文件信息
     * @return FileStorage
     */
    public FileStorage getFileStorageVerify(FileInfo fileInfo) {
        FileStorage fileStorage = getFileStorage(fileInfo.getPlatform());
        if (fileStorage == null) {
            throw new FileStorageException("没有找到对应的存储平台");
        }
        return fileStorage;
    }

    /**
     * 上传文件，成功返回文件信息，失败返回null
     *
     * @param pre 文件预处理对象
     * @return 文件信息
     */
    public FileInfo upload(UploadPretreatment pre) {
        MultipartFile file = pre.getFileWrapper();
        if (file == null) {
            throw new FileStorageException("文件不允许为null");
        }
        if (pre.getPlatform() == null) {
            throw new FileStorageException("platform 不允许为 null");
        }
        FileInfo fileInfo = new FileInfo();
        // 文件基本信息
        fileInfo.setCreateTime(new Date());
        fileInfo.setSize(file.getSize());
        fileInfo.setOriginalFilename(file.getOriginalFilename());
        fileInfo.setExt(FileNameUtil.getSuffix(file.getOriginalFilename()));
        fileInfo.setObjectId(pre.getObjectId());
        fileInfo.setObjectType(pre.getObjectType());
        fileInfo.setPath(pre.getPath());
        fileInfo.setPlatform(pre.getPlatform());
        fileInfo.setAttr(pre.getAttr());
        // 保存文件名称
        if (StrUtil.isNotBlank(pre.getSaveFilename())) {
            fileInfo.setFilename(pre.getSaveFilename());
        } else {
            fileInfo.setFilename(IdUtil.objectId() + ((StrUtil.isEmpty(fileInfo.getExt())) ? StrUtil.EMPTY : "." + fileInfo.getExt()));
        }
        // ContentType处理
        if (pre.getContentType() != null) {
            fileInfo.setContentType(pre.getContentType());
        } else if (pre.getFileWrapper().getContentType() != null) {
            fileInfo.setContentType(pre.getFileWrapper().getContentType());
        } else {
            fileInfo.setContentType(tikaFactory.getTika().detect(fileInfo.getFilename()));
        }

        byte[] thumbnailBytes = pre.getThumbnailBytes();
        if (thumbnailBytes != null) {
            fileInfo.setThSize((long) thumbnailBytes.length);
            if (StrUtil.isNotBlank(pre.getSaveThFilename())) {
                fileInfo.setThFilename(pre.getSaveThFilename() + pre.getThumbnailSuffix());
            } else {
                fileInfo.setThFilename(fileInfo.getFilename() + pre.getThumbnailSuffix());
            }
            fileInfo.setThContentType(tikaFactory.getTika().detect(thumbnailBytes, fileInfo.getThFilename()));
        }

        FileStorage fileStorage = getFileStorage(pre.getPlatform());
        if (fileStorage == null) {
            throw new FileStorageException("没有找到对应的存储平台");
        }
        // 处理切面
        return new UploadAspectChain(aspectList, (_fileInfo, _pre, _fileStorage, _fileRecorder) -> {
            // 保存数据
            if (_fileStorage.save(_fileInfo, _pre)) {
                if (_fileRecorder.record(_fileInfo)) {
                    return _fileInfo;
                }
            }
            return null;
        }).next(fileInfo, pre, fileStorage, fileRecorder);
    }

    /**
     * 根据URL获取FileInfo
     *
     * @param url URL
     * @return 文件信息
     */
    public FileInfo getFileInfoByUrl(String url) {
        return fileRecorder.getByUrl(url);
    }

    /**
     * 根据 URL 删除文件
     *
     * @param url 文件URL地址
     * @return 是否删除成功
     */
    public boolean delete(String url) {
        return delete(getFileInfoByUrl(url));
    }

    /**
     * 根据URL删除文件
     *
     * @param url       文件 URL 地址
     * @param predicate 文件相关信息条件
     * @return 是否删除成功
     */
    public boolean delete(String url, Predicate<FileInfo> predicate) {
        return delete(getFileInfoByUrl(url), predicate);
    }

    /**
     * 根据文件信息删除文件
     *
     * @param fileInfo 文件信息
     * @return 是否删除成功
     */
    public boolean delete(FileInfo fileInfo) {
        return delete(fileInfo, null);
    }

    /**
     * 根据条件删除文件
     *
     * @param fileInfo  文件信息
     * @param predicate 删除条件
     * @return 是否删除成功
     */
    public boolean delete(FileInfo fileInfo, Predicate<FileInfo> predicate) {
        if (fileInfo == null) {
            return true;
        }
        if (predicate != null && !predicate.test(fileInfo)) {
            return false;
        }
        FileStorage fileStorage = getFileStorage(fileInfo.getPlatform());
        if (fileStorage == null) {
            throw new FileStorageException("没有找到对应的存储平台");
        }
        return new DeleteAspectChain(aspectList, (_fileInfo, _fileStorage, _fileRecoreder) -> {
            if (_fileStorage.delete(_fileInfo)) {
                return _fileRecoreder.delete(_fileInfo.getUrl());
            }
            return false;
        }).next(fileInfo, fileStorage, fileRecorder);
    }

    /**
     * 文件是否存在
     *
     * @param url 文件URL
     * @return 是否存在
     */
    public boolean exists(String url) {
        return exists(getFileInfoByUrl(url));
    }

    /**
     * 判断文件是否存在
     *
     * @param fileInfo 文件信息
     * @return 是否存在
     */
    public boolean exists(FileInfo fileInfo) {
        if (fileInfo == null) {
            return false;
        }
        return new ExistsAspectChain(aspectList, (_fileInfo, _fileStorage) ->
                _fileStorage.exists(_fileInfo)
        ).next(fileInfo, getFileStorageVerify(fileInfo));
    }

    /**
     * 获取文件下载器
     *
     * @param fileInfo 文件信息
     * @return 文件下载器
     */
    public Downloader download(FileInfo fileInfo) {
        return new Downloader(fileInfo, aspectList, getFileStorageVerify(fileInfo), Downloader.TARGET_FILE);
    }

    /**
     * 根据文件 URL 获取文件下载器
     *
     * @param url URL地址
     * @return 文件下载器
     */
    public Downloader download(String url) {
        return download(getFileInfoByUrl(url));
    }

    /**
     * 获取缩略图文件下载器
     *
     * @param fileInfo 文件信息
     * @return 文件下载器
     */
    public Downloader downloadTh(FileInfo fileInfo) {
        return new Downloader(fileInfo, aspectList, getFileStorageVerify(fileInfo), Downloader.TARGET_TH_FILE);
    }

    /**
     * 根据文件缩略图 URL 获取文件下载器
     *
     * @param url URL地址
     * @return 文件下载器
     */
    public Downloader downloadTh(String url) {
        return downloadTh(getFileInfoByUrl(url));
    }

    /**
     * 创建上传预处理器
     *
     * @return 文件预处理
     */
    public UploadPretreatment of() {
        UploadPretreatment pre = new UploadPretreatment();
        pre.setFileStorageService(self);
        pre.setPlatform(properties.getDefaultPlatform());
        pre.setThumbnailSuffix(properties.getThumbnailSuffix());
        return pre;
    }

    /**
     * 根据 MultipartFile 创建上传预处理器
     *
     * @param file 上海窜文件
     * @return 文件预处理器
     */
    public UploadPretreatment of(MultipartFile file) {
        UploadPretreatment pre = of();
        pre.setFileWrapper(new MultipartFileWrapper(file));
        return pre;
    }

    /**
     * 根据 byte[] 创建上传预处理器，name为空
     *
     * @param bytes 字节数组
     * @return 文件预处理器
     */
    public UploadPretreatment of(byte[] bytes) {
        UploadPretreatment pre = of();
        String contentType = tikaFactory.getTika().detect(bytes);
        pre.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile("", "", contentType, bytes)));
        return pre;
    }

    /**
     * 根据 InputStream 创建上传预处理器，name为空
     *
     * @param in 文件输入流
     * @return 文件预处理器
     */
    public UploadPretreatment of(InputStream in) {
        try {
            byte[] bytes = IoUtil.readBytes(in);
            String contentType = tikaFactory.getTika().detect(bytes);
            UploadPretreatment pre = of();
            pre.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile("", "", contentType, bytes)));
            return pre;
        } catch (Exception e) {
            throw new FileStorageException("根据 InputStream 创建上传预处理器失败!", e);
        }
    }

    /**
     * 根据File创建上传与处理器，originalFilename为file的name
     *
     * @param file 文件
     * @return 文件预处理器
     */
    public UploadPretreatment of(File file) {
        try {
            UploadPretreatment pre = of();
            String contentType = tikaFactory.getTika().detect(file);
            pre.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile(file.getName(), file.getName(), contentType, Files.newInputStream(file.toPath()))));
            return pre;
        } catch (Exception e) {
            throw new FileStorageException("根据 File 创建上传预处理器失败!", e);
        }
    }

    /**
     * 根据URL创建上传预处理器，originalFilename 将尝试自动识别，识别不到则为空字符串
     *
     * @param url URL地址
     * @return 文件预处理器
     */
    public UploadPretreatment of(URL url) {
        try {
            UploadPretreatment pre = of();
            URLConnection conn = url.openConnection();
            // 尝试获取文件名
            String name = "";
            String disposition = conn.getHeaderField("Content-Disposition");
            if (StrUtil.isNotBlank(disposition)) {
                name = ReUtil.get("filename=\"(.*?)\"", disposition, 1);
                if (StrUtil.isBlank(name)) {
                    name = StrUtil.subAfter(disposition, "filename=", true);
                }
            }
            if (StrUtil.isBlank(name)) {
                final String path = url.getPath();
                name = StrUtil.subSuf(path, path.lastIndexOf('/') + 1);
                if (StrUtil.isNotBlank(name)) {
                    name = URLUtil.decode(name, StandardCharsets.UTF_8);
                }
            }
            byte[] bytes = IoUtil.readBytes(conn.getInputStream());
            String contentType = tikaFactory.getTika().detect(bytes, name);
            pre.setFileWrapper(new MultipartFileWrapper(new MockMultipartFile(url.toString(), name, contentType, bytes)));
            return pre;
        } catch (Exception e) {
            throw new FileStorageException("根据 url 创建上传预处理器失败!", e);
        }
    }

    /**
     * 根据 URI 创建上传预处理器，originalFilename将尝试自动识别，识别不到则为空字符串
     *
     * @param uri uri
     * @return 文件预处理器
     */
    public UploadPretreatment of(URI uri) {
        try {
            return of(uri.toURL());
        } catch (Exception e) {
            throw new FileStorageException("根据 URI 创建上传预处理器失败!", e);
        }
    }

    /**
     * 根据 url 字符串创建上传预处理，兼容Spring的classpath路径、文件路径、HTTP路径等，originalFilename将尝试自动识别，识别不到则为空字符串
     *
     * @param url 文件地址
     * @return 文件预处理器
     */
    public UploadPretreatment of(String url) {
        try {
            return of(URLUtil.url(url));
        } catch (Exception e) {
            throw new FileStorageException("根据 url：" + url + "创建上传预处理器失败!", e);
        }
    }

    @Override
    public void destroy() {
        for (FileStorage fileStorage : fileStorageList) {
            try {
                fileStorage.close();
                log.info("销毁存储平台 {} 成功", fileStorage.getPlatform());
            } catch (Exception e) {
                log.error("销毁存储平台 {} 失败, {}", fileStorage.getPlatform(), e.getMessage(), e);
            }
        }
    }
}
