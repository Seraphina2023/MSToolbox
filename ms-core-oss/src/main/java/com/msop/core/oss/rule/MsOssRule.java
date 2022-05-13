package com.msop.core.oss.rule;

import com.msop.core.tool.constant.StringConstant;
import com.msop.core.tool.utils.DateUtil;
import com.msop.core.tool.utils.FileUtil;
import com.msop.core.tool.utils.StringUtil;
import lombok.AllArgsConstructor;

/**
 * 默认存储桶规则
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
public class MsOssRule implements OssRule {
    /**
     * 租户模式
     */
    private final Boolean tenantMode;
    /**
     * 获取存储桶规则
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    @Override
    public String bucketName(String bucketName) {
        String prefix = (tenantMode) ? AuthUtil.getTenantId().concat(StringConstant.DASH) : StringConstant.EMPTY;
        return prefix + bucketName;
    }

    /**
     * 获取文件名规则
     *
     * @param originalFilename 文件名
     * @return String
     */
    @Override
    public String fileName(String originalFilename) {
        return "upload" + StringConstant.SLASH + DateUtil.toDay() + StringConstant.SLASH + StringUtil.randomUUID() + StringConstant.DOT + FileUtil.getFileExtension(originalFilename);
    }
}
