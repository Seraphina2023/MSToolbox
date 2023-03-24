package tech.msop.core.file.storage.aspect;

import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.platform.FileStorage;
import tech.msop.core.file.storage.recorder.FileRecorder;

/**
 * 上传切面调用链结束回调
 */
public interface UploadAspectChainCallback {

    FileInfo run(FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage, FileRecorder fileRecorder);
}
