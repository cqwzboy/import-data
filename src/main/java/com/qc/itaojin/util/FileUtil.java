package com.qc.itaojin.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * Created by fuqinqin on 2018/6/26.
 */
public class FileUtil {

    /**
     * 追加写入字符串
     * */
    public static void writeStringTo(String content, String path){
        if(StringUtils.isBlank(content) || StringUtils.isEmpty(path)){
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(new File(path), true);
            fos.write((content+"\n").getBytes("UTF-8"));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
