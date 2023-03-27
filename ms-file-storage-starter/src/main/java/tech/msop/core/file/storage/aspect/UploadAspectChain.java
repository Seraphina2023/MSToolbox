package tech.msop.core.file.storage.aspect;

import lombok.Getter;
import lombok.Setter;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.platform.FileStorage;
import tech.msop.core.file.storage.recorder.FileRecorder;

import java.util.Iterator;

/**
 * 上传的切面调用链
 */
@Getter
@Setter
public class UploadAspectChain {
    private UploadAspectChainCallback callback;
    private Iterator<FileStorageAspect> aspectIterator;

    public UploadAspectChain(Iterable<FileStorageAspect> aspect, UploadAspectChainCallback callback) {
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
    public FileInfo next(FileInfo fileInfo, UploadPretreatment pre,FileStorage fileStorage, FileRecorder fileRecorder) {
        if (aspectIterator.hasNext()) {
            return aspectIterator.next().uploadAround(this,fileInfo,pre,fileStorage,fileRecorder);
        }else{
            return callback.run(fileInfo,pre,fileStorage,fileRecorder);
        }
    }
}
