package com.example.zhulinping.studydemo.backuprestore.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import com.example.zhulinping.studydemo.backuprestore.bean.SmsInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhulinping on 2017/9/25.
 */

public class SmsUtils {
    private static String TAG = "SmsUtils";
    public static final Uri SMS_URI = Uri.parse("content://sms/");
    private static SmsUtils instance;

    public static SmsUtils getInstance() {
        if (instance == null) {
            instance = new SmsUtils();
        }
        return instance;
    }

    public SmsUtils() {

    }

    //获取短信
    public ArrayList<SmsInfo> getSmsList(Context context) {
        ArrayList<SmsInfo> smsList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(SMS_URI, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                SmsInfo smsInfo = new SmsInfo();
                smsInfo.setId(cursor.getString(cursor.getColumnIndex(BaseColumns._ID)));
                smsInfo.setThreadId(cursor.getString(cursor.getColumnIndex(Telephony.Sms.THREAD_ID)));
                smsInfo.setErrorCode(cursor.getString(cursor.getColumnIndex(Telephony.Sms.ERROR_CODE)));
                smsInfo.setLocked(cursor.getString(cursor.getColumnIndex(Telephony.Sms.LOCKED)));
                smsInfo.setPerson(cursor.getString(cursor.getColumnIndex(Telephony.Sms.PERSON)));
                smsInfo.setProtocol(cursor.getString(cursor.getColumnIndex(Telephony.Sms.PROTOCOL)));
                smsInfo.setRead(cursor.getString(cursor.getColumnIndex(Telephony.Sms.READ)));
                smsInfo.setReplyPathPresent(cursor.getString(cursor.getColumnIndex(Telephony.Sms.REPLY_PATH_PRESENT)));
                smsInfo.setSubject(cursor.getString(cursor.getColumnIndex(Telephony.Sms.SUBJECT)));
                smsInfo.setSeen(cursor.getString(cursor.getColumnIndex(Telephony.Sms.SEEN)));
                smsInfo.setServiceCenter(cursor.getString(cursor.getColumnIndex(Telephony.Sms.SERVICE_CENTER)));
                smsInfo.setStatus(cursor.getString(cursor.getColumnIndex(Telephony.Sms.STATUS)));
                smsInfo.setAddress(cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS)));
                smsInfo.setType(cursor.getString(cursor.getColumnIndex(Telephony.Sms.TYPE)));
                smsInfo.setDate(cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE)));
                smsInfo.setBody(cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY)));
                smsList.add(smsInfo);
            }
        } finally {
            cursor.close();
        }
        return smsList;
    }

    //将短信存入文件
    public String smsBackup(Context context, ArrayList<SmsInfo> list) {
        if (null == list || list.size() == 0) {
            return null;
        }
        Gson gson = new Gson();
        String smsJsonStr = gson.toJson(list);
        if (smsJsonStr == null || "".equals(smsJsonStr)) {
            return "";
        }
        String filePath = FileUtils.getSmsFilePath();
        File file = new File(filePath);
        Writer writer = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            //打开文件进行写入
            writer = new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8");
            writer.write(smsJsonStr);
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //读取文件中待还原短信
    public ArrayList<SmsInfo> readSmsFromFile(Context context, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        ArrayList<SmsInfo> smsList = new ArrayList<>();
        BufferedReader reader = null;
        //打开并从文件中读取数据到StringBuilder
        try {
            //打开文件进行读取
            InputStream in = new FileInputStream(filePath);
            reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            String smsJsonStr = jsonString.toString();
            if (null == smsJsonStr || "".equals(smsJsonStr)) {
                return null;
            }
            Gson gson = new Gson();
            List<SmsInfo> list = gson.fromJson(jsonString.toString(), new TypeToken<List<SmsInfo>>() {
            }.getType());
            if (null == list || list.size() == 0) {
                return null;
            }
            smsList.clear();
            smsList.addAll(list);
            return smsList;
        } catch (FileNotFoundException e) {
            //当应用首次启动时，没有数据会抛出异常，异常不用捕获
            Log.d(TAG, "未找到文件！");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //写入还原短信
    public boolean smsRestore(Context context, ArrayList<SmsInfo> list) {
        if (null == list || list.size() == 0) {
            return false;
        }
        try {
            Uri uri = Uri.parse("content://sms");
            //context.getContentResolver().delete(uri, null, null);
            for (SmsInfo smsInfo : list) {
                ContentValues values = new ContentValues();
                //values.put(BaseColumns._ID, smsInfo.getId());
                // values.put(Telephony.Sms.THREAD_ID, smsInfo.getThreadId());
                values.put(Telephony.Sms.ADDRESS, smsInfo.getAddress());
                values.put(Telephony.Sms.PERSON, smsInfo.getPerson());
                values.put(Telephony.Sms.PROTOCOL, smsInfo.getProtocol());
                values.put(Telephony.Sms.READ, smsInfo.getRead());
                values.put(Telephony.Sms.DATE, smsInfo.getDate());
                values.put(Telephony.Sms.STATUS, smsInfo.getStatus());
                values.put(Telephony.Sms.TYPE, smsInfo.getType());
                values.put(Telephony.Sms.REPLY_PATH_PRESENT, smsInfo.getReplyPathPresent());
                values.put(Telephony.Sms.SUBJECT, smsInfo.getSubject());
                values.put(Telephony.Sms.SERVICE_CENTER, smsInfo.getServiceCenter());
                values.put(Telephony.Sms.LOCKED, smsInfo.getLocked());
                values.put(Telephony.Sms.ERROR_CODE, smsInfo.getErrorCode());
                values.put(Telephony.Sms.SEEN, smsInfo.getSeen());
                values.put(Telephony.Sms.BODY, smsInfo.getBody());
                context.getContentResolver().insert(uri, values);
            }
            Log.d("zlp", "sms restore");
            return true;
        } catch (Exception e) {
            Log.d("zlp", "sms error " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
