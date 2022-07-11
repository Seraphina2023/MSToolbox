package com.msop.core.boot.properties;

import com.msop.core.tool.utils.PathUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.lang.Nullable;

/**
 * 文件上传配置
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("ms.upload")
public class MsUploadProperties {

    /**
     * 文件保存目录，默认：jar 包同级目录
     */
    @Nullable
    private String savePath = PathUtil.getJarPath();
}
