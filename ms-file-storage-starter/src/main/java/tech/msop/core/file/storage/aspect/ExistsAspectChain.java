package tech.msop.core.file.storage.aspect;

import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.platform.FileStorage;

import java.util.Iterator;

/**
 * 文件是否存在的切面调用链
 */
@Getter
@Setter
public class ExistsAspectChain {
    private ExistsAspectChainCallback callback;
    private Iterator<FileStorageAspect> aspectIterator;

    public ExistsAspectChain(Iterable<FileStorageAspect> aspect, ExistsAspectChainCallback callback) {
        this.aspectIterator = aspect.iterator();
        this.callback = callback;
    }

    /**
     * 调用下一个切面
     *
     * @param fileInfo    文件信息
     * @param fileStorage 文件存储
     * @return boolean
     */
    public boolean next(FileInfo fileInfo, FileStorage fileStorage) {
        if (aspectIterator.hasNext()) {
            return aspectIterator.next().existsAround(this, fileInfo, fileStorage);
        } else {
            return callback.run(fileInfo, fileStorage);
        }
    }
}
