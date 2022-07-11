package com.msop.core.boot.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.msop.core.boot.properties.MsFileProperties;
import com.msop.core.tool.utils.DateUtil;
import com.msop.core.tool.utils.SpringUtil;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 上传文件封装类
 *
 * @author ruozhuliufeng
 */
@Data
public class LocalFile {
    /**
     * 上传文件在附件表中的ID
     */
    private Object fileId;
    /**
     * 上传文件
     */
    @JsonIgnore
    private MultipartFile file;
    /**
     * 文件外网地址
     */
    private String domain;
    /**
     * 上传分类文件夹
     */
    private String dir;

    /**
     * 上传物理路径
     */
    private String uploadPath;

    /**
     * 上传虚拟路径
     */
    private String uploadVirtualPath;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 真实文件名
     */
    private String originalFileName;

    /**
     * 文件配置
     */
    private static MsFileProperties fileProperties;

    private static MsFileProperties getFileProperties() {
        if (fileProperties == null) {
            fileProperties = SpringUtil.getBean(MsFileProperties.class);
        }
        return fileProperties;
    }

    public LocalFile(MultipartFile file, String dir) {
        this.dir = dir;
        this.file = file;
        this.fileName = file.getName();
        this.originalFileName = file.getOriginalFilename();
        this.uploadPath = MsFileUtil.formatUrl(File.separator + getFileProperties().getUploadRealPath() + File.separator + dir + File.separator + DateUtil.format(DateUtil.now(), "yyyyMMdd") + File.separator + this.originalFileName);
        this.uploadVirtualPath = MsFileUtil.formatUrl(getFileProperties().getUploadCtxPath().replace(getFileProperties().getContextPath(), "") + File.separator + dir + File.separator + DateUtil.format(DateUtil.now(), "yyyyMMdd") + File.separator + this.originalFileName);
        this.domain = getFileProperties().getUploadDomain();
    }

    public LocalFile(MultipartFile file, String dir, String uploadPath, String uploadVirtualPath) {
        this(file, dir);
        if (null != uploadPath) {
            this.uploadPath = MsFileUtil.formatUrl(uploadPath);
            this.uploadVirtualPath = MsFileUtil.formatUrl(uploadVirtualPath);
        }
    }

    /**
     * 图片上传
     */
    public void transfer() {
        transfer(getFileProperties().getCompress());
    }

    /**
     * 图片上传
     *
     * @param compress 是否压缩
     */
    public void transfer(boolean compress) {
        IFileProxy fileFactory = FileProxyManager.me().getDefaultFileProxyFactory();
        this.transfer(fileFactory, compress);
    }

    /**
     * 图片上传
     *
     * @param fileFactory 文件代理工厂
     * @param compress    是否压缩
     */
    public void transfer(IFileProxy fileFactory, boolean compress) {
        try {
            File file = new File(uploadPath);

            if (null != fileFactory) {
                String[] path = fileFactory.path(file, dir);
                this.uploadPath = path[0];
                this.uploadVirtualPath = path[1];
                file = fileFactory.rename(file, path[0]);
            }

            File pfile = file.getParentFile();
            if (!pfile.exists()) {
                pfile.mkdirs();
            }
            this.file.transferTo(file);
            if (compress) {
                fileFactory.compress(this.uploadPath);
            }
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

}
