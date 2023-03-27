package tech.msop.core.file.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * MultipartFile 的包装类
 */
public class MultipartFileWrapper implements MultipartFile {
    /**
     * 文件名
     */
    @Setter
    private String name;
    /**
     * 原始文件名
     */
    @Setter
    private String originalFilename;
    /**
     * 内容类型
     */
    @Setter
    private String contentType;

    @Getter
    @Setter
    private MultipartFile multipartFile;

    public MultipartFileWrapper(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    @Override
    public String getName() {
        return name != null ? name : multipartFile.getName();
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename != null ? originalFilename : multipartFile.getOriginalFilename();
    }

    @Override
    public String getContentType() {
        return contentType != null ? contentType : multipartFile.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return multipartFile.isEmpty();
    }

    @Override
    public long getSize() {
        return multipartFile.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return multipartFile.getBytes();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return multipartFile.getInputStream();
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        multipartFile.transferTo(dest);
    }

    @Override
    public Resource getResource() {
        return multipartFile.getResource();
    }

    @Override
    public void transferTo(Path dest) throws IOException, IllegalStateException {
        multipartFile.transferTo(dest);
    }
}
