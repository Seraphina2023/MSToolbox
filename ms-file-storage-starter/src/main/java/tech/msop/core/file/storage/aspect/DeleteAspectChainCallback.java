package tech.msop.core.file.storage.aspect;

import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.platform.FileStorage;
import tech.msop.core.file.storage.recorder.FileRecorder;

/**
 * 删除切面调用链结束回调
 */
public interface DeleteAspectChainCallback {

    boolean run(FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder);
}
