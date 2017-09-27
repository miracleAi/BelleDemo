package com.example.zhulinping.studydemo.backuprestore.utils;

/**
 * Created by zhulinping on 2017/9/26.
 */

public interface BackupReatoreListener {
    void onTotal(int typeId, int count);

    void onProgress(int typeId, int progress);
}
