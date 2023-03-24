package tech.msop.core.file.storage.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties("ms.file-storage")
public class MsFileStorageProperties {
    /**
     * 默认存储平台
     */
    private String defaultPlatform = "local";

    /**
     * 缩略图后缀 例如 .min.jpg  .png
     */
    private String thumbnailSuffix = ".min.jpg";
    /**
     * 本地存储
     */
    private List<Local> local = new ArrayList<>();
    /**
     * 本地存储
     */
    private List<LocalPlus> localPlus = new ArrayList<>();
    /**
     * 华为云 OBS
     */
    private List<HuaweiObs> huaweiObs = new ArrayList<>();
    /**
     * 阿里云 OSS
     */
    private List<AliyunOss> aliyunOss = new ArrayList<>();
    /**
     * 七牛云 Kodo
     */
    private List<QiniuKodo> qiniuKodo = new ArrayList<>();
    /**
     * 腾讯云 COS
     */
    private List<TencentCos> tencentCos = new ArrayList<>();
    /**
     * 百度云 BOS
     */
    private List<BaiduBos> baiduBos = new ArrayList<>();
    /**
     * 又拍云 USS
     */
    private List<UpyunUSS> upyunUSS = new ArrayList<>();
    /**
     * MinIO
     */
    private List<MinIO> minio = new ArrayList<>();
    /**
     * AWS S3
     */
    private List<AwsS3> awsS3 = new ArrayList<>();
    /**
     * FTP
     */
    private List<FTP> ftp = new ArrayList<>();
    /**
     * SFTP
     */
    private List<SFTP> sftp = new ArrayList<>();
    /**
     * WebDAV
     */
    private List<WebDAV> webDav = new ArrayList<>();
    /**
     * 谷歌云存储
     */
    private List<GoogleCloud> googleCloud = new ArrayList<>();

    /**
     * 本地存储
     */
    @Data
    public static class Local {
        /**
         * 本地存储路径
         */
        private String basePtah;
        /**
         * 本地存储访问路径
         */
        private String[] pathPatterns = new String[0];
        /**
         * 启用本地存储
         */
        private Boolean enableStorage = false;
        /**
         * 启用本地访问
         */
        private Boolean enableAccess = false;
        /**
         * 存储平台
         */
        private String platform = "local";
        /**
         * 访问域名
         */
        private String domain = "";
    }

    /**
     * 本地存储升级版
     */
    @Data
    public static class LocalPlus {
        /**
         * 本地存储路径
         */
        private String basePtah;
        /**
         * 存储路径，上传的文件都会存储在这个路径下面，默认"/"，注意以"/"结尾
         */
        private String storagePath = "/";
        /**
         * 本地存储访问路径
         */
        private String[] pathPatterns = new String[0];
        /**
         * 启用本地存储
         */
        private Boolean enableStorage = false;
        /**
         * 启用本地访问
         */
        private Boolean enableAccess = false;
        /**
         * 存储平台
         */
        private String platform = "local";
        /**
         * 访问域名
         */
        private String domain = "";
    }

    /**
     * 云存储基础配置
     */
    @Data
    public static class BaseStorageConfig {
        /**
         * 访问密钥
         */
        private String accessKey;
        /**
         * 密钥
         */
        private String secretKey;
        /**
         * 端点
         */
        private String endPoint;
        /**
         * 存储桶名称
         */
        private String bucketName;
        /**
         * 访问域名
         */
        private String domain = "";
        /**
         * 启用存储
         */
        private Boolean enableStorage = false;
        /**
         * 存储平台标识
         */
        private String platform = "";
        /**
         * 基础路径
         */
        private String basePath = "";
    }

    /**
     * 华为云 OBS
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class HuaweiObs extends BaseStorageConfig {

    }

    /**
     * 阿里云 OSS
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class AliyunOss extends BaseStorageConfig {

    }

    /**
     * 七牛云 Kodo
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class QiniuKodo extends BaseStorageConfig {
    }

    /**
     * 腾讯云 COS
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class TencentCos extends BaseStorageConfig {
        /**
         * 区域
         */
        private String region;
    }

    /**
     * 百度云 BOS
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class BaiduBos extends BaseStorageConfig {

    }

    /**
     * 又拍云 USS
     */
    @Data
    public static class UpyunUSS {
        /**
         * 用户名
         */
        private String username;
        /**
         * 密码
         */
        private String password;
        /**
         * 存储桶名称
         */
        private String bucketName;
        /**
         * 访问域名
         */
        private String domain;
        /**
         * 存储平台
         */
        private String platform;
        /**
         * 基础路径
         */
        private String basePath;
        /**
         * 启用存储
         */
        private Boolean enableStorage = false;

    }

    /**
     * MinIO
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MinIO extends BaseStorageConfig {
    }

    /**
     * AWS S3
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class AwsS3 extends BaseStorageConfig {
        private String region;
    }

    /**
     * FTP
     */
    @Data
    public static class FTP {
        /**
         * 主机
         */
        private String host;
        /**
         * 端口，默认 21
         */
        private int port = 21;
        /**
         * 用户名，默认 anonymous(匿名)
         */
        private String user = "anonymous";
        /**
         * 密码，默认空
         */
        private String password = "";
        /**
         * 编码 默认UTF-8
         */
        private Charset charset = StandardCharsets.UTF_8;
        /**
         * 连接超时时长，单位毫秒，默认10秒 {@link org.apache.commons.net.SocketClient#setConnectTimeout(int)}
         */
        private long connectionTimeout = 10 * 1000;
        /**
         * Socket连接超时时长，单位毫秒，默认10秒 {@link org.apache.commons.net.SocketClient#setSoTimeout(int)}
         */
        private long soTimeout = 10 * 1000;
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
        private Boolean isActive = false;
        /**
         * 访问域名
         */
        private String domain;
        /**
         * 启用存储
         */
        private Boolean enableStorage = false;
        /**
         * 存储平台
         */
        private String platform = "";
        /**
         * 基础路径
         */
        private String basePath = "";
        /**
         * 存储路径，上传的文件都会存储在这个路径下，注意以"/"结尾
         */
        private String storagePath = "/";
    }

    /**
     * SFTP
     */
    @Data
    public static class SFTP {
        /**
         * 主机
         */
        private String host;
        /**
         * 端口，默认22
         */
        private int port = 22;
        /**
         * 用户名
         */
        private String user;
        /**
         * 密码
         */
        private String password;
        /**
         * 私钥路径
         */
        private String privateKeyPath;
        /**
         * 编码,默认 UTF-8
         */
        private Charset charset = StandardCharsets.UTF_8;
        /**
         * 连接超时时长，单位毫秒，默认10秒
         */
        private long connectionTimeout = 10 * 1000;
        /**
         * 访问域名
         */
        private String domain = "";
        /**
         * 启用存储
         */
        private Boolean enableStorage = false;
        /**
         * 存储标识
         */
        private String platform = "";
        /**
         * 基础路径
         */
        private String basePath = "";
        /**
         * 存储路径，上传的文件都会存储在这个路径下面，注意以"/"结尾
         */
        private String storagePath = "/";
    }

    /**
     * WebDAV
     */
    @Data
    public static class WebDAV {
        /**
         * 服务器地址，注意以"/"结尾
         */
        private String server;
        /**
         * 用户名
         */
        private String user;
        /**
         * 密码
         */
        private String password;
        /**
         * 访问域名
         */
        private String domain = "";
        /**
         * 启用存储
         */
        private Boolean enableStorage = false;
        /**
         * 存储平台
         */
        private String platform = "";
        /**
         * 基础路径
         */
        private String basePath = "";
        /**
         * 存储路径，上传的文件都会存储在这个路径下面，注意以"/"结尾
         */
        private String storagePath = "/";
    }

    /**
     * Google Cloud
     */
    @Data
    public static class GoogleCloud {
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
         * 启用存储
         */
        private Boolean enableStorage = false;
        /**
         * 存储平台
         */
        private String platform = "";
        /**
         * 基础路径
         */
        private String basePath = "";

    }
}
