package tech.msop.core.file.storage.aspect;

import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.platform.FileStorage;

import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * 下载的切面调用链
 */
@Getter
@Setter
public class DownloadAspectChain {
    private DownloadAspectChainCallback callback;
    private Iterator<FileStorageAspect> aspectIterator;

    public DownloadAspectChain(Iterable<FileStorageAspect> aspect, DownloadAspectChainCallback callback) {
        this.aspectIterator = aspect.iterator();
        this.callback = callback;
    }

    /**
     * 调用下一个切面
     *
     * @param fileInfo 文件信息
     * @param consumer consumer
     */
    public void next(FileInfo fileInfo, FileStorage fileStorage, Consumer<InputStream> consumer) {
        if (aspectIterator.hasNext()) {
            aspectIterator.next().downloadAround(this, fileInfo, fileStorage, consumer);
        } else {
            callback.run(fileInfo, fileStorage, consumer);
        }
    }
}
