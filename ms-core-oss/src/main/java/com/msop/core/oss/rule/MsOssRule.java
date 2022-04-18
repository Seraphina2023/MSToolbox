package com.msop.core.oss.rule;

import com.msop.core.common.constant.StringConstant;
import com.msop.core.common.utils.DateUtil;
import com.msop.core.common.utils.FileUtil;
import com.msop.core.common.utils.StringUtil;
import lombok.AllArgsConstructor;

/**
 * 默认存储桶规则
 *
 * @author ruozhuliufeng
 */
@AllArgsConstructor
public class MsOssRule implements OssRule {
    /**
     * 获取存储桶规则
     *
     * @param bucketName 存储桶名称
     * @return String
     */
    @Override
    public String bucketName(String bucketName) {
        return bucketName;
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
