package com.banmatrip.alert.constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Miracle Xu on 2017/8/31.
 */
public class FileUtils {

    //文件名处理，防止乱码
    //参数：文件名 title，文件类型 fileType
    public static String fileNameUtil(String title,String fileType) throws UnsupportedEncodingException {
        StringBuffer fileName = new StringBuffer();
        String file = URLEncoder.encode(title,"UTF-8");
        if(StaticInfo.XLS.equals(fileType)){
            fileName = fileName.append("attachment;filename*=UTF-8''").append(file).append(StaticInfo.XLS);
        }else{
            fileName = fileName.append("attachment;filename*=UTF-8''").append(file).append(StaticInfo.XLSX);
        }
        return fileName.toString();
    }

    //文件类型处理（.xls还是.xlsx）
    //参数：文件类型 fileType
    //返回值：true .xlsx, false .xls
    public static Boolean fileTypeUtil(String fileType){
        Boolean isXSSF = !StaticInfo.XLS.equals(fileType);
        return isXSSF;
    }
}
