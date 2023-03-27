package tech.msop.core.file.storage;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import tech.msop.core.file.storage.aspect.DownloadAspectChain;
import tech.msop.core.file.storage.aspect.DownloadThAspectChain;
import tech.msop.core.file.storage.aspect.FileStorageAspect;
import tech.msop.core.file.storage.exception.FileStorageException;
import tech.msop.core.file.storage.platform.FileStorage;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 下载器
 */
public class Downloader {
    /**
     * 下载目标：文件
     */
    public static final int TARGET_FILE = 1;
    /**
     * 下载目标：缩略图文件
     */
    public static final int TARGET_TH_FILE = 2;

    private final FileStorage fileStorage;
    private final FileInfo fileInfo;
    private final List<FileStorageAspect> aspectList;
    private final Integer target;
    private ProgressListener progressListener;

    /**
     * 构造下载器
     *
     * @param target 下载目标：{@link Downloader#TARGET_FILE}下载文件，{@link Downloader#TARGET_TH_FILE}下载缩略图文件
     */
    public Downloader(FileInfo fileInfo, List<FileStorageAspect> aspectList, FileStorage fileStorage, Integer target) {
        this.fileStorage = fileStorage;
        this.aspectList = aspectList;
        this.fileInfo = fileInfo;
        this.target = target;
    }

    /**
     * 设置下载监听器
     *
     * @param progressListener 提供参数，表示已传输字节
     * @return Downloader
     */
    public Downloader setProgressMonitor(Consumer<Long> progressListener) {
        return setProgressMonitor((progressSize, allSize) -> progressListener.accept(progressSize));
    }

    /**
     * 设置下载进度监听器
     *
     * @param progessListener 提供两个参数，第一个是progressSize 已传输字节数，第二个是allSize 总字节数
     * @return Downloader
     */
    public Downloader setProgressMonitor(BiConsumer<Long, Long> progessListener) {
        return setProgressMonitor(new ProgressListener() {
            @Override
            public void start() {

            }

            @Override
            public void progress(long progressSize, long allSize) {
                progessListener.accept(progressSize, allSize);
            }

            @Override
            public void finish() {

            }
        });
    }

    /**
     * 设置下载进度监听器
     *
     * @param progressMonitor 监听器
     * @return Downloader
     */
    public Downloader setProgressMonitor(ProgressListener progressMonitor) {
        this.progressListener = progressMonitor;
        return this;
    }

    public void inputStream(Consumer<InputStream> consumer) {
        if (target == TARGET_FILE) {
            // 下载文件
            new DownloadAspectChain(aspectList, (_fileInfo, _fileStorage, _consumer) ->
                    _fileStorage.download(_fileInfo, _consumer)
            ).next(fileInfo, fileStorage, in ->
                    consumer.accept(progressListener == null ? in : new ProgressInputStream(in, progressListener, fileInfo.getSize()))
            );
        } else if (target == TARGET_TH_FILE) {
            // 下载缩略图文件
            // 下载文件
            new DownloadThAspectChain(aspectList, (_fileInfo, _fileStorage, _consumer) ->
                    _fileStorage.download(_fileInfo, _consumer)
            ).next(fileInfo, fileStorage, in ->
                    consumer.accept(progressListener == null ? in : new ProgressInputStream(in, progressListener, fileInfo.getThSize()))
            );
        } else {
            throw new FileStorageException("没有找到对应的下载目标，请设置 target 参数");
        }
    }

    /**
     * 下载byte数组
     *
     * @return 数组
     */
    public byte[] bytes() {
        byte[][] bytes = new byte[1][];
        inputStream(in -> bytes[0] = IoUtil.readBytes(in));
        return bytes[0];
    }

    /**
     * 下载到指定文件
     *
     * @param file 文件
     */
    public void file(File file) {
        inputStream(in -> FileUtil.writeFromStream(in, file));
    }

    /**
     * 下载到指定文件
     *
     * @param filename 文件名
     */
    public void file(String filename) {
        inputStream(in -> FileUtil.writeFromStream(in, filename));
    }

    /**
     * 下载到指定输出流
     *
     * @param out 输出流
     */
    public void outputStream(OutputStream out) {
        inputStream(in -> IoUtil.copy(in, out));
    }
}
