package com.msop.core.boot.file;

public class MsFileUtil {


    public static String formatUrl(String url){
        return url.replaceAll("\\\\", "/");
    }
}
