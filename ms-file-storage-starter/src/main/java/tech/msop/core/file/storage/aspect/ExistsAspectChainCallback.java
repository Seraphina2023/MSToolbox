package tech.msop.core.file.storage.aspect;

import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.platform.FileStorage;

/**
 * 文件是否存在切面调用链结束回调
 */
public interface ExistsAspectChainCallback {

    boolean run(FileInfo fileInfo, FileStorage fileStorage);
}
