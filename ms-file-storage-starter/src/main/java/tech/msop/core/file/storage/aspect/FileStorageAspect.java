package tech.msop.core.file.storage.aspect;

import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.platform.FileStorage;
import tech.msop.core.file.storage.recorder.FileRecorder;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * 文件服务切面接口，用来干预文件上传、删除等
 */
public interface FileStorageAspect {

    /**
     * 上传，成功返回文件信息，失败返回null
     *
     * @param chain        切面调用链
     * @param fileInfo     文件信息
     * @param pre          预处理对象
     * @param fileStorage  文件存储
     * @param fileRecorder recorder
     * @return 上传后的文件信息
     */
    default FileInfo uploadAround(UploadAspectChain chain, FileInfo fileInfo, UploadPretreatment pre, FileStorage fileStorage, FileRecorder fileRecorder) {
        return chain.next(fileInfo, pre, fileStorage, fileRecorder);
    }

    /**
     * 删除文件，成功发挥true
     *
     * @param chain        删除切面调用链
     * @param fileInfo     文件信息
     * @param fileStorage  文件存储
     * @param fileRecorder recoreder
     * @return 是否删除
     */
    default boolean deleteAround(DeleteAspectChain chain, FileInfo fileInfo, FileStorage fileStorage, FileRecorder fileRecorder) {
        return chain.next(fileInfo, fileStorage, fileRecorder);
    }

    /**
     * 校验文件是否存在，成功发挥true
     *
     * @param chain       删除切面调用链
     * @param fileInfo    文件信息
     * @param fileStorage 文件存储
     * @return 文件是否存在
     */
    default boolean existsAround(ExistsAspectChain chain, FileInfo fileInfo, FileStorage fileStorage) {
        return chain.next(fileInfo, fileStorage);
    }

    /**
     * 下载文件，成功返回文件内容
     *
     * @param chain       删除切面调用链
     * @param fileInfo    文件信息
     * @param fileStorage 文件存储
     * @param consumer    consumer
     */
    default void downloadAround(DownloadAspectChain chain, FileInfo fileInfo, FileStorage fileStorage, Consumer<InputStream> consumer) {
        chain.next(fileInfo, fileStorage, consumer);
    }

    /**
     * 下载文件缩略图，成功返回文件内容
     *
     * @param chain       删除切面调用链
     * @param fileInfo    文件信息
     * @param fileStorage 文件存储
     * @param consumer    consumer
     */
    default void downloadThAround(DownloadThAspectChain chain, FileInfo fileInfo, FileStorage fileStorage, Consumer<InputStream> consumer) {
        chain.next(fileInfo, fileStorage, consumer);
    }
}
