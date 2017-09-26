package com.example.zhulinping.studydemo.backuprestore.utils;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhulinping on 2017/9/25.
 */

public class FileUtils {
    public static final String FORDER_PATH = "/msgcenter/";

    public static String getContactsFilePath() {
        String forderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + FORDER_PATH;
        File file = new File(forderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return forderPath + "vcard.vcf";
    }

    public static Uri getUri() {
        Uri uri = null;
        String filePath = getContactsFilePath();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            uri = Uri.fromFile(file);
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSmsFilePath() {
        String forderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + FORDER_PATH;
        File file = new File(forderPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return forderPath + "sms.json";
    }
    //删除文件
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        return file.delete();
    }
}
