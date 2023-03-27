package tech.msop.core.file.storage.recorder;

import tech.msop.core.file.storage.FileInfo;

/**
 * 文件记录者接口
 *
 * @author ruozhuliufeng
 */
public interface FileRecorder {
    /**
     * 保存文件记录
     *
     * @param fileInfo 文件信息
     * @return boolean
     */
    boolean record(FileInfo fileInfo);

    /**
     * 根据URL获取文件记录
     *
     * @param url URL
     * @return 文件记录
     */
    FileInfo getByUrl(String url);

    /**
     * 根据URL删除文件记录
     *
     * @param url 文件URL
     * @return boolean
     */
    boolean delete(String url);
}
