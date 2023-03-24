package tech.msop.core.file.storage.platform;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.upyun.RestManager;
import com.upyun.UpException;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Response;
import okhttp3.ResponseBody;
import tech.msop.core.file.storage.FileInfo;
import tech.msop.core.file.storage.UploadPretreatment;
import tech.msop.core.file.storage.exception.FileStorageException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * 又拍云 USS 存储
 */
@Getter
@Setter
public class UpyunUssFileStorage implements FileStorage {
    // 存储平台参数
    private String platform;
    private String username;
    private String password;
    private String bucketName;
    private String domain;
    private String basePath;

    private RestManager client;

    /**
     * 单例模式运行，不需要每次使用完在销毁了
     */
    public RestManager getClient() {
        if (client == null) {
            client = new RestManager(bucketName, username, password);
        }
        return client;
    }

    @Override
    public boolean save(FileInfo fileInfo, UploadPretreatment pre) {
        String newFileKey = basePath + fileInfo.getPath() + fileInfo.getFilename();
        fileInfo.setBasePath(basePath);
        fileInfo.setUrl(domain + newFileKey);
        RestManager manager = getClient();
        try (InputStream in = pre.getFileWrapper().getInputStream()) {
            HashMap<String, String> params = new HashMap<>();
            params.put(RestManager.PARAMS.CONTENT_TYPE.getValue(), fileInfo.getContentType());
            try (Response result = manager.writeFile(newFileKey, in, params)) {
                if (!result.isSuccessful()) {
                    throw new UpException(result.toString());
                }
            }
            byte[] thumbnailBytes = pre.getThumbnailBytes();
            if (thumbnailBytes != null) {
                // 上传缩略图
                String newThFileKey = basePath + fileInfo.getPath() + fileInfo.getThFilename();
                fileInfo.setThUrl(domain + newThFileKey);
                HashMap<String, String> thParams = new HashMap<>();
                thParams.put(RestManager.PARAMS.CONTENT_TYPE.getValue(), fileInfo.getThContentType());
                Response thResult = manager.writeFile(newThFileKey, new ByteArrayInputStream(thumbnailBytes), thParams);
                if (!thResult.isSuccessful()) {
                    throw new UpException(thResult.toString());
                }
            }
            return true;
        } catch (IOException | UpException e) {
            try {
                manager.deleteFile(newFileKey, null).close();
            } catch (Exception ignored) {
            }
            throw new FileStorageException("文件上传阿里云失败!platform:" + platform + ",filename:" + fileInfo.getOriginalFilename(), e);
        }
    }

    @Override
    public boolean delete(FileInfo fileInfo) {
        RestManager client = getClient();
        String file = fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename();
        String thFile = fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename();
        try (Response ignored = fileInfo.getThFilename() != null ? client.deleteFile(thFile, null) : null;
             Response ignored2 = client.deleteFile(file, null)) {
            return true;
        } catch (Exception e) {
            throw new FileStorageException("文件删除失败!fileInfo:" + fileInfo);
        }
    }

    @Override
    public boolean exists(FileInfo fileInfo) {
        try (Response response = getClient().getFileInfo(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())){
            return StrUtil.isNotBlank(response.header("x-upyun-file-size"));
        }catch (IOException | UpException e){
            throw new FileStorageException("判断文件是否存在失败!fileInfo:"+fileInfo,e);
        }
    }

    @Override
    public void download(FileInfo fileInfo, Consumer<InputStream> consumer) {
        try (Response response = getClient().readFile(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getFilename())){
            ResponseBody body = response.body();
            InputStream in = body == null? null :body.byteStream();
            if (body == null){
                throw new FileStorageException("文件下载失败，结果为null! fileInfo:"+fileInfo);
            }
            if (!response.isSuccessful()){
                throw new UpException(IoUtil.read(in, StandardCharsets.UTF_8));
            }
            consumer.accept(in);
        } catch (IOException | UpException e) {
            throw new FileStorageException("文件下载失败!fileInfo:" + fileInfo, e);
        }
    }

    @Override
    public void downloadTh(FileInfo fileInfo, Consumer<InputStream> consumer) {
        if (StrUtil.isBlank(fileInfo.getThFilename())) {
            throw new FileStorageException("缩略图文件下载失败,文件不存在!fileInfo:" + fileInfo);
        }
        try (Response response = getClient().readFile(fileInfo.getBasePath() + fileInfo.getPath() + fileInfo.getThFilename())){
            ResponseBody body = response.body();
            InputStream in = body == null? null :body.byteStream();
            if (body == null){
                throw new FileStorageException("缩略图文件下载失败，结果为null! fileInfo:"+fileInfo);
            }
            if (!response.isSuccessful()){
                throw new UpException(IoUtil.read(in, StandardCharsets.UTF_8));
            }
            consumer.accept(in);
        } catch (IOException | UpException e) {
            throw new FileStorageException("缩略图文件下载失败!fileInfo:" + fileInfo, e);
        }
    }

    @Override
    public void close() {
        if (client != null) {
            client = null;
        }
    }
}
