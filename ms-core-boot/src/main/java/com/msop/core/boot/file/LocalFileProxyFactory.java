package com.msop.core.boot.file;

import com.msop.core.boot.properties.MsFileProperties;
import com.msop.core.tool.constant.StringConstant;
import com.msop.core.tool.utils.DateUtil;
import com.msop.core.tool.utils.ImageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * 文件代理类
 *
 * @author ruozhuliufeng
 */
public class LocalFileProxyFactory implements IFileProxy {
    /**
     * 文件配置
     */
    private static MsFileProperties fileProperties;

    private static MsFileProperties getMsFileProperties() {
        if (fileProperties == null) {
            fileProperties = new MsFileProperties();
        }
        return fileProperties;
    }

    /**
     * 返回路径 [物理路径] [虚拟路径]
     *
     * @param file 文件对象
     * @param dir  文件目录
     * @return 路径
     */
    @Override
    public String[] path(File file, String dir) {
        // 避免网络延迟导致时间不同步
        long time = System.nanoTime();
        StringBuilder uploadPath = new StringBuilder()
                .append(getFileDir(dir, getMsFileProperties().getUploadRealPath()))
                .append(time)
                .append(getFileExt(file.getName()));
        StringBuilder virtualPath = new StringBuilder()
                .append(getFileDir(dir, getMsFileProperties().getUploadCtxPath()))
                .append(time)
                .append(getFileExt(file.getName()));
        return new String[]{MsFileUtil.formatUrl(uploadPath.toString()), MsFileUtil.formatUrl(virtualPath.toString())};
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return 后缀
     */
    public static String getFileExt(String fileName) {
        if (!fileName.contains(StringConstant.DOT)) {
            return ".jpg";
        } else {
            return fileName.substring(fileName.lastIndexOf(StringConstant.DOT));
        }
    }

    /**
     * 获取文件保存地址
     *
     * @param dir     文件目录
     * @param saveDir 文件保存地址
     * @return 文件保存地址
     */
    public static String getFileDir(String dir, String saveDir) {
        StringBuilder newFileDir = new StringBuilder();
        newFileDir.append(saveDir)
                .append(File.separator)
                .append(dir)
                .append(File.separator)
                .append(DateUtil.format(DateUtil.now(), "yyyyMMdd"))
                .append(File.separator);
        return newFileDir.toString();
    }

    /**
     * 文件重命名策略
     *
     * @param file 文件对象
     * @param path 文件路径
     * @return 文件名
     */
    @Override
    public File rename(File file, String path) {
        File dest = new File(path);
        file.renameTo(dest);
        return dest;
    }

    /**
     * 图片压缩
     *
     * @param path 图片路径
     */
    @Override
    public void compress(String path) {
        try{
            ImageUtil.zoomScale(ImageUtil.readImage(path),new FileOutputStream(new File(path)),null,getMsFileProperties().getCompressScale(),getMsFileProperties().getCompressFlag());
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
