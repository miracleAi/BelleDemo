package com.example.zhulinping.studydemo.backuprestore.bean;

/**
 * Created by zhulinping on 2017/9/25.
 */

public class SmsInfo {
    //电话号码
    private String address;
    //日期
    private String date;
    //短信类型
    private String type;
    //短信内容
    private String body;

    public SmsInfo() {
    }

    public SmsInfo(String address, String date, String type, String body) {
        this.address = address;
        this.date = date;
        this.type = type;
        this.body = body;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "SmsInfo [address=" + address + ", date=" + date + ", type="
                + type + ", body=" + body + "]";
    }


}
