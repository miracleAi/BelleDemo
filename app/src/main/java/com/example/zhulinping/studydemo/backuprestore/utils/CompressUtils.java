package com.example.zhulinping.studydemo.backuprestore.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by zhulinping on 2017/9/27.
 */

public class CompressUtils {
    private static int BUFFERSIZE = 1024;

    public static byte[] compressByGzip(String str) {
        if (null == str || str.length() <= 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes());
            gzip.close();
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null; //返回错误
        }
    }

    public static String unCompressByGzip(String str) {
        if (null == str || str.length() <= 0) {
            return str;
        }
        try {
            InputStream inputStream = new FileInputStream(str);
            // 使用默认缓冲区大小创建新的输入流
            GZIPInputStream gzip = new GZIPInputStream(inputStream);
            BufferedReader reader = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            return "500";//错误
        }
        return null;
    }
}