package com.example.zhulinping.studydemo.backuprestore.bean;

/**
 * Created by zhulinping on 2017/9/25.
 */

public class SmsInfo {
    private String id;
    private String threadId;
    //电话号码
    private String address;
    private String person;
    private String protocol;
    private String read;
    //日期
    private String date;
    private String status;
    //短信类型
    private String type;
    private String replyPathPresent;
    private String subject;
    private String serviceCenter;
    private String locked;
    private String errorCode;
    private String seen;
    //短信内容
    private String body;

    public SmsInfo() {
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReplyPathPresent() {
        return replyPathPresent;
    }

    public void setReplyPathPresent(String replyPathPresent) {
        this.replyPathPresent = replyPathPresent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getServiceCenter() {
        return serviceCenter;
    }

    public void setServiceCenter(String serviceCenter) {
        this.serviceCenter = serviceCenter;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "SmsInfo [id=" + id + ", threadId=" + threadId + ", address=" + address + ", person" + person +
                ", protocol=" + protocol + ", read=" + read + ", date=" + date + ", status=" + status +
                ", type=" + type + ", replyPathPresent=" + replyPathPresent + ", subject=" + subject +
                ", locked=" + locked + ", errorCode=" + errorCode + ", seen=" + seen +
                ", serviceCenter" + serviceCenter + ", body=" + body + "]";
    }


}
