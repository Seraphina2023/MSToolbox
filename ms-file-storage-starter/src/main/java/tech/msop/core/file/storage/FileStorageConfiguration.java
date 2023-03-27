package tech.msop.core.file.storage;

import cn.hutool.core.collection.CollUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.msop.core.file.storage.aspect.FileStorageAspect;
import tech.msop.core.file.storage.platform.*;
import tech.msop.core.file.storage.properties.MsFileStorageProperties;
import tech.msop.core.file.storage.recorder.DefaultFileRecorder;
import tech.msop.core.file.storage.recorder.FileRecorder;
import tech.msop.core.file.storage.tika.DefaultTikaFactory;
import tech.msop.core.file.storage.tika.TikaFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 文件存储配置
 *
 * @author ruozhuliufeng
 */
@AutoConfiguration
@Slf4j
@EnableConfigurationProperties(MsFileStorageProperties.class)
@AllArgsConstructor
@ConditionalOnMissingBean(FileStorageService.class)
public class FileStorageConfiguration implements WebMvcConfigurer {
    private final MsFileStorageProperties properties;
    private final ApplicationContext applicationContext;

    /**
     * Add handlers to serve static resources such as images, js, and, css
     * files from specific locations under web application root, the classpath,
     * and others.
     *
     * @param registry
     * @see ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        for (MsFileStorageProperties.Local local : properties.getLocal()) {
            if (local.getEnableAccess()) {
                registry.addResourceHandler(local.getPathPatterns())
                        .addResourceLocations("file:" + local.getBasePtah());
            }
        }
        for (MsFileStorageProperties.LocalPlus local : properties.getLocalPlus()) {
            if (local.getEnableAccess()) {
                registry.addResourceHandler(local.getPathPatterns())
                        .addResourceLocations("file:" + local.getStoragePath());
            }
        }
    }

    /**
     * 本地存储Bean
     *
     * @return 本地存储
     */
    @Bean
    public List<LocalFileStorage> localFileStorageList() {
        return properties.getLocal().stream().map(local -> {
            if (!local.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", local.getPlatform());
            LocalFileStorage localFileStorage = new LocalFileStorage();
            localFileStorage.setPlatform(local.getPlatform());
            localFileStorage.setBasePath(local.getBasePtah());
            localFileStorage.setDomain(local.getDomain());
            return localFileStorage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 本地存储 Plus Bean
     *
     * @return 本地存储Plus
     */
    @Bean
    public List<LocalPlusFileStorage> localPlusFileStorageList() {
        return properties.getLocalPlus().stream().map(local -> {
            if (!local.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", local.getPlatform());
            LocalPlusFileStorage localPlusFileStorage = new LocalPlusFileStorage();
            localPlusFileStorage.setPlatform(local.getPlatform());
            localPlusFileStorage.setBasePath(local.getBasePtah());
            localPlusFileStorage.setStoragePath(local.getStoragePath());
            localPlusFileStorage.setDomain(local.getDomain());
            return localPlusFileStorage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 华为云 OBS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.obs.services.ObsClient")
    public List<HuaweiObsFileStorage> huaweiObsFileStorageList() {
        return properties.getHuaweiObs().stream().map(obs -> {
            if (!obs.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", obs.getPlatform());
            HuaweiObsFileStorage storage = new HuaweiObsFileStorage();
            storage.setPlatform(obs.getPlatform());
            storage.setAccessKey(obs.getAccessKey());
            storage.setSecretKey(obs.getSecretKey());
            storage.setDomain(obs.getDomain());
            storage.setBucketName(obs.getBucketName());
            storage.setEndPoint(obs.getEndPoint());
            storage.setBasePath(obs.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 阿里云 OSS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.aliyun.oss.OSS")
    public List<AliyunOssFileStorage> aliyunOssFileStorageList() {
        return properties.getAliyunOss().stream().map(oss -> {
            if (!oss.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", oss.getPlatform());
            AliyunOssFileStorage storage = new AliyunOssFileStorage();
            storage.setPlatform(oss.getPlatform());
            storage.setAccessKey(oss.getAccessKey());
            storage.setSecretKey(oss.getSecretKey());
            storage.setDomain(oss.getDomain());
            storage.setBucketName(oss.getBucketName());
            storage.setEndPoint(oss.getEndPoint());
            storage.setBasePath(oss.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 七牛云 Kodo 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.qiniu.storage.UploadManager")
    public List<QiniuKodoFileStorage> qiniuKodoFileStorageList() {
        return properties.getAliyunOss().stream().map(kodo -> {
            if (!kodo.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", kodo.getPlatform());
            QiniuKodoFileStorage storage = new QiniuKodoFileStorage();
            storage.setPlatform(kodo.getPlatform());
            storage.setAccessKey(kodo.getAccessKey());
            storage.setSecretKey(kodo.getSecretKey());
            storage.setDomain(kodo.getDomain());
            storage.setBucketName(kodo.getBucketName());
            storage.setBasePath(kodo.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 腾讯云 COS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.qiniu.storage.UploadManager")
    public List<TencentCosFileStorage> tencentCosFileStorageList() {
        return properties.getTencentCos().stream().map(cos -> {
            if (!cos.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", cos.getPlatform());
            TencentCosFileStorage storage = new TencentCosFileStorage();
            storage.setPlatform(cos.getPlatform());
            storage.setAccessKey(cos.getAccessKey());
            storage.setSecretKey(cos.getSecretKey());
            storage.setRegion(cos.getRegion());
            storage.setDomain(cos.getDomain());
            storage.setBucketName(cos.getBucketName());
            storage.setBasePath(cos.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 百度云 BOS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.baidubce.services.bos.BosClient")
    public List<BaiduBosFileStorage> baiduBosFileStorageList() {
        return properties.getBaiduBos().stream().map(bos -> {
            if (!bos.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", bos.getPlatform());
            BaiduBosFileStorage storage = new BaiduBosFileStorage();
            storage.setPlatform(bos.getPlatform());
            storage.setAccessKey(bos.getAccessKey());
            storage.setSecretKey(bos.getSecretKey());
            storage.setDomain(bos.getDomain());
            storage.setBucketName(bos.getBucketName());
            storage.setEndPoint(bos.getEndPoint());
            storage.setBasePath(bos.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 又拍云 USS 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.upyun.RestManager")
    public List<UpyunUssFileStorage> upyunUssFileStorageList() {
        return properties.getUpyunUSS().stream().map(uss -> {
            if (!uss.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", uss.getPlatform());
            UpyunUssFileStorage storage = new UpyunUssFileStorage();
            storage.setPlatform(uss.getPlatform());
            storage.setUsername(uss.getUsername());
            storage.setPassword(uss.getPassword());
            storage.setDomain(uss.getDomain());
            storage.setBucketName(uss.getBucketName());
            storage.setBasePath(uss.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * MinIO 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "io.minio.MinioClient")
    public List<MinIOFileStorage> minIOFileStorageList() {
        return properties.getMinio().stream().map(minio -> {
            if (!minio.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", minio.getPlatform());
            MinIOFileStorage storage = new MinIOFileStorage();
            storage.setPlatform(minio.getPlatform());
            storage.setAccessKey(minio.getAccessKey());
            storage.setSecretKey(minio.getSecretKey());
            storage.setEndPoint(minio.getEndPoint());
            storage.setDomain(minio.getDomain());
            storage.setBucketName(minio.getBucketName());
            storage.setBasePath(minio.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * AWS S3 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.amazonaws.services.s3.AmazonS3")
    public List<AwsS3FileStorage> amazonS3FileStorageList() {
        return properties.getAwsS3().stream().map(s3 -> {
            if (!s3.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", s3.getPlatform());
            AwsS3FileStorage storage = new AwsS3FileStorage();
            storage.setPlatform(s3.getPlatform());
            storage.setAccessKey(s3.getAccessKey());
            storage.setSecretKey(s3.getSecretKey());
            storage.setRegion(s3.getRegion());
            storage.setEndPoint(s3.getEndPoint());
            storage.setDomain(s3.getDomain());
            storage.setBucketName(s3.getBucketName());
            storage.setBasePath(s3.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * FTP 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = {
            "org.apache.commons.net.ftp.FTPClient",
            "cn.hutool.extra.ftp.Ftp"
    })
    public List<FtpFileStorage> ftpFileStorageList() {
        return properties.getFtp().stream().map(ftp -> {
            if (!ftp.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", ftp.getPlatform());
            FtpFileStorage storage = new FtpFileStorage();
            storage.setPlatform(ftp.getPlatform());
            storage.setHost(ftp.getHost());
            storage.setPort(ftp.getPort());
            storage.setUser(ftp.getUser());
            storage.setPassword(ftp.getPassword());
            storage.setCharset(ftp.getCharset());
            storage.setConnectionTimeout(ftp.getConnectionTimeout());
            storage.setSoTimeout(ftp.getSoTimeout());
            storage.setServerLanguageCode(ftp.getServerLanguageCode());
            storage.setSystemKey(ftp.getSystemKey());
            storage.setIsActive(ftp.getIsActive());
            storage.setStoragePath(ftp.getStoragePath());
            storage.setDomain(ftp.getDomain());
            storage.setBasePath(ftp.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * SFTP 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = {
            "com.jcraft.jsch.ChannelSftp",
            "cn.hutool.extra.ftp.Ftp"
    })
    public List<SftpFileStorage> sftpFileStorageList() {
        return properties.getSftp().stream().map(sftp -> {
            if (!sftp.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", sftp.getPlatform());
            SftpFileStorage storage = new SftpFileStorage();
            storage.setPlatform(sftp.getPlatform());
            storage.setHost(sftp.getHost());
            storage.setPort(sftp.getPort());
            storage.setUser(sftp.getUser());
            storage.setPassword(sftp.getPassword());
            storage.setPrivateKeyPath(sftp.getPrivateKeyPath());
            storage.setCharset(sftp.getCharset());
            storage.setConnectionTimeout(sftp.getConnectionTimeout());
            storage.setStoragePath(sftp.getStoragePath());
            storage.setDomain(sftp.getDomain());
            storage.setBasePath(sftp.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * WebDAV 存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.github.sardine.Sardine")
    public List<WebDavFileStorage> webDavFileStorageList() {
        return properties.getWebDav().stream().map(webDAV -> {
            if (!webDAV.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", webDAV.getPlatform());
            WebDavFileStorage storage = new WebDavFileStorage();
            storage.setPlatform(webDAV.getPlatform());
            storage.setServer(webDAV.getServer());
            storage.setUser(webDAV.getUser());
            storage.setPassword(webDAV.getPassword());
            storage.setStoragePath(webDAV.getStoragePath());
            storage.setDomain(webDAV.getDomain());
            storage.setBasePath(webDAV.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Google云存储 Bean
     */
    @Bean
    @ConditionalOnClass(name = "com.google.cloud.storage.Storage")
    public List<GoogleCloudStorage> googleCloudStorageList() {
        return properties.getGoogleCloud().stream().map(cloud -> {
            if (!cloud.getEnableStorage()) {
                return null;
            }
            log.info("加载存储平台:{}", cloud.getPlatform());
            GoogleCloudStorage storage = new GoogleCloudStorage();
            storage.setPlatform(cloud.getPlatform());
            storage.setProjectId(cloud.getProjectId());
            storage.setBucketName(cloud.getBucketName());
            storage.setCredentialsPath(cloud.getCredentialsPath());
            storage.setDomain(cloud.getDomain());
            storage.setBasePath(cloud.getBasePath());
            return storage;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Bean
    @ConditionalOnMissingBean(FileRecorder.class)
    public FileRecorder fileRecorder() {
        log.warn("没有找到 FileRecorder 的实现类,文件上传之外的部分功能无法正常使用,必须实现该接口才能使用完整功能!");
        return new DefaultFileRecorder();
    }

    /**
     * Tika工厂类型，用于识别上传的文件的MINE
     *
     * @return TikaFactory
     */
    @Bean
    @ConditionalOnMissingBean(TikaFactory.class)
    public TikaFactory tikaFactory() {
        return new DefaultTikaFactory();
    }


    /**
     * 文件存储服务
     *
     * @param fileRecorder     文件存储记录
     * @param fileStorageLists 存储方法实现
     * @param aspectList       切面调用链
     * @param tikaFactory      Tika工厂
     * @return 文件存储服务
     */
    @Bean
    public FileStorageService fileStorageService(FileRecorder fileRecorder,
                                                 List<List<? extends FileStorage>> fileStorageLists,
                                                 List<FileStorageAspect> aspectList,
                                                 TikaFactory tikaFactory) {
        this.initDetect();
        FileStorageService service = new FileStorageService();
        service.setFileStorageList(new CopyOnWriteArrayList<>());
        fileStorageLists.forEach(service.getFileStorageList()::addAll);
        service.setFileRecorder(fileRecorder);
        service.setProperties(properties);
        service.setAspectList(new CopyOnWriteArrayList<>(aspectList));
        service.setTikaFactory(tikaFactory);
        return service;
    }

    /**
     * 对FileStorageService 注入自己的代理对象，不然会导致针对 FileStorageService 的代理方法不生效
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshedEvent() {
        FileStorageService service = applicationContext.getBean(FileStorageService.class);
        service.setSelf(service);
    }

    public void initDetect() {
        String template = "检测到 {} 配置,但是没有找到对应的存储库,所以无法加载存储平台!相关配置请查看文档";
        if (CollUtil.isNotEmpty(properties.getHuaweiObs()) && doesNotExistClass("com.obs.services.ObsClient")) {
            log.warn(template, "华为云 OBS");
        }
        if (CollUtil.isNotEmpty(properties.getAliyunOss()) && doesNotExistClass("com.aliyun.oss.OSS")) {
            log.warn(template, "阿里云 OSS");
        }
        if (CollUtil.isNotEmpty(properties.getQiniuKodo()) && doesNotExistClass("com.qiniu.storage.UploadManager")) {
            log.warn(template, "七牛云 Kodo");
        }
        if (CollUtil.isNotEmpty(properties.getTencentCos()) && doesNotExistClass("com.qcloud.cos.COSClient")) {
            log.warn(template, "腾讯云 COS");
        }
        if (CollUtil.isNotEmpty(properties.getBaiduBos()) && doesNotExistClass("com.baidubce.services.bos.BosClient")) {
            log.warn(template, "百度云 BOS");
        }
        if (CollUtil.isNotEmpty(properties.getUpyunUSS()) && doesNotExistClass("com.upyun.RestManager")) {
            log.warn(template, "又拍云 USS");
        }
        if (CollUtil.isNotEmpty(properties.getMinio()) && doesNotExistClass("io.minio.MinioClient")) {
            log.warn(template, "MinIO");
        }
        if (CollUtil.isNotEmpty(properties.getAwsS3()) && doesNotExistClass("com.amazonaws.services.s3.AmazonS3")) {
            log.warn(template, "Amazon S3");
        }
        if (CollUtil.isNotEmpty(properties.getFtp()) && doesNotExistClass("org.apache.commons.net.ftp.FTPClient") || doesNotExistClass("cn.hutool.extra.ftp.Ftp")) {
            log.warn(template, "FTP");
        }
        if (CollUtil.isNotEmpty(properties.getSftp()) && doesNotExistClass("com.jcraft.jsch.ChannelSftp") || doesNotExistClass("cn.hutool.extra.ftp.Ftp")) {
            log.warn(template, "SFTP");
        }
        if (CollUtil.isNotEmpty(properties.getWebDav()) && doesNotExistClass("com.github.sardine.Sardine")) {
            log.warn(template, "WebDAV");
        }
        if (CollUtil.isNotEmpty(properties.getGoogleCloud()) && doesNotExistClass("com.google.cloud.storage.Storage")) {
            log.warn(template, "谷歌云存储");
        }
    }

    /**
     * 判断是否引入指定Class
     *
     * @param name Class名
     * @return 是否引入
     */
    public static boolean doesNotExistClass(String name) {
        try {
            Class.forName(name);
            return false;
        } catch (ClassNotFoundException e) {
            return true;
        }
    }
}
