package tech.msop.core.file.storage.aspect;

import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.platform.FileStorage;
import tech.msop.core.file.storage.recorder.FileRecorder;

import java.util.Iterator;

/**
 * 删除的切面调用链
 */
@Getter
@Setter
public class DeleteAspectChain {
    private DeleteAspectChainCallback callback;
    private Iterator<FileStorageAspect> aspectIterator;

    public DeleteAspectChain(Iterable<FileStorageAspect> aspect, DeleteAspectChainCallback callback) {
        this.aspectIterator = aspect.iterator();
        this.callback = callback;
    }

    /**
     * 调用下一个切面
     *
     * @param fileInfo     文件信息
     * @param fileRecorder FileRecoreder
     * @return boolean
     */
    public boolean next(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder) {
        if (aspectIterator.hasNext()) {
            return aspectIterator.next().deleteAround(this,fileInfo,fileStorage,fileRecorder);
        }else{
            return callback.run(fileInfo,fileStorage,fileRecorder);
        }
    }
}
