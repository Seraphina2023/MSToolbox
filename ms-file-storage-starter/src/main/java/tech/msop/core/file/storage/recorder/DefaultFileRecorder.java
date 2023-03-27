package tech.msop.core.file.storage.recorder;

import tech.msop.core.file.storage.FileInfo;

/**
 * 默认的文件记录保存者类
 */
public class DefaultFileRecorder implements FileRecorder{
    /**
     * 保存文件记录
     *
     * @param fileInfo 文件信息
     * @return boolean
     */
    @Override
    public boolean record(FileInfo fileInfo) {
        return false;
    }

    /**
     * 根据URL获取文件记录
     *
     * @param url URL
     * @return 文件记录
     */
    @Override
    public FileInfo getByUrl(String url) {
        return null;
    }

    /**
     * 根据URL删除文件记录
     *
     * @param url 文件URL
     * @return boolean
     */
    @Override
    public boolean delete(String url) {
        return false;
    }
}
