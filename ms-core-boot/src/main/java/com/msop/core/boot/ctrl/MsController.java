package com.msop.core.boot.ctrl;

import com.msop.core.boot.file.LocalFile;
import com.msop.core.boot.file.MsFileUtil;
import com.msop.core.secure.model.MsUser;
import com.msop.core.secure.utils.AuthUtil;
import com.msop.core.tool.constant.StringConstant;
import com.msop.core.tool.model.Result;
import com.msop.core.tool.utils.Charsets;
import com.msop.core.tool.utils.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * MS 控制器封装类
 *
 * @author ruozhuliufeng
 */
public class MsController {

    /**
     * ============================     REQUEST    =================================================
     */

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取request
     */
    public HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    public MsUser getUser() {
        return AuthUtil.getUser();
    }

    /** ============================     API_RESULT    =================================================  */

    /**
     * 返回ApiResult
     *
     * @param data
     * @return R
     */
    public <T> Result<T> data(T data) {
        return Result.succeed(data);
    }

    /**
     * 返回ApiResult
     *
     * @param data
     * @param message
     * @return Result
     */
    public <T> Result<T> data(T data, String message) {
        return Result.succeed(data, message);
    }

    /**
     * 返回ApiResult
     *
     * @param data
     * @param message
     * @param code
     * @return Result
     */
    public <T> Result<T> data(T data, String message, int code) {
        return Result.succeed(code, data, message);
    }

    /**
     * 返回ApiResult
     *
     * @param message
     * @return R
     */
    public Result success(String message) {
        return Result.succeed(message);
    }

    /**
     * 返回ApiResult
     *
     * @param message
     * @return Result
     */
    public Result fail(String message) {
        return Result.failed(message);
    }


    /**============================     FILE    =================================================  */

    /**
     * 获取ZhisuanFile封装类
     *
     * @param file
     * @return
     */
    public LocalFile getFile(MultipartFile file) {
        return MsFileUtil.getFile(file);
    }

    /**
     * 获取ZhisuanFile封装类
     *
     * @param file
     * @param dir
     * @return
     */
    public LocalFile getFile(MultipartFile file, String dir) {
        return MsFileUtil.getFile(file, dir);
    }

    /**
     * 获取ZhisuanFile封装类
     *
     * @param file
     * @param dir
     * @param path
     * @param virtualPath
     * @return
     */
    public LocalFile getFile(MultipartFile file, String dir, String path, String virtualPath) {
        return MsFileUtil.getFile(file, dir, path, virtualPath);
    }

    /**
     * 获取ZhisuanFile封装类
     *
     * @param files
     * @return
     */
    public List<LocalFile> getFiles(List<MultipartFile> files) {
        return MsFileUtil.getFiles(files);
    }

    /**
     * 获取ZhisuanFile封装类
     *
     * @param files
     * @param dir
     * @return
     */
    public List<LocalFile> getFiles(List<MultipartFile> files, String dir) {
        return MsFileUtil.getFiles(files, dir);
    }

    /**
     * 获取ZhisuanFile封装类
     *
     * @param files
     * @param path
     * @param virtualPath
     * @return
     */
    public List<LocalFile> getFiles(List<MultipartFile> files, String dir, String path, String virtualPath) {
        return MsFileUtil.getFiles(files, dir, path, virtualPath);
    }
    /**
     * 下载文件
     *
     * @param file 文件
     * @return {ResponseEntity}
     * @throws IOException io异常
     */
    protected ResponseEntity<ResourceRegion> download(File file) throws IOException {
        String fileName = file.getName();
        return download(file, fileName);
    }

    /**
     * 下载
     *
     * @param file     文件
     * @param fileName 生成的文件名
     * @return {ResponseEntity}
     * @throws IOException io异常
     */
    protected ResponseEntity<ResourceRegion> download(File file, String fileName) throws IOException {
        Resource resource = new FileSystemResource(file);
        return download(resource, fileName);
    }

    /**
     * 下载
     *
     * @param resource 资源
     * @param fileName 生成的文件名
     * @return {ResponseEntity}
     * @throws IOException io异常
     */
    protected ResponseEntity<ResourceRegion> download(Resource resource, String fileName) throws IOException {
        HttpServletRequest request = WebUtil.getRequest();
        String header = request.getHeader(HttpHeaders.USER_AGENT);
        // 避免空指针
        header = header == null ? StringConstant.EMPTY : header.toUpperCase();
        HttpStatus status;
        String msie= "MSIE";
        String trident= "TRIDENT";
        String edge= "EDGE";
        if (header.contains(msie) || header.contains(trident) || header.contains(edge)) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.CREATED;
        }
        // 断点续传
        long position = 0;
        long count = resource.contentLength();
        String range = request.getHeader(HttpHeaders.RANGE);
        if (null != range) {
            status = HttpStatus.PARTIAL_CONTENT;
            String[] rangeRange = range.replace("bytes=", StringConstant.EMPTY).split(StringConstant.DASH);
            position = Long.parseLong(rangeRange[0]);
            if (rangeRange.length > 1) {
                long end = Long.parseLong(rangeRange[1]);
                count = end - position + 1;
            }
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        String encodeFileName = UriUtils.encode(fileName, Charsets.UTF_8);
        // 兼容各种浏览器下载：
        // https://blog.robotshell.org/2012/deal-with-http-header-encoding-for-file-download/
        String disposition = "attachment;" +
                "filename=\"" + encodeFileName + "\";" +
                "filename*=utf-8''" + encodeFileName;
        headers.set(HttpHeaders.CONTENT_DISPOSITION, disposition);
        return new ResponseEntity<>(new ResourceRegion(resource, position, count), headers, status);
    }

}
