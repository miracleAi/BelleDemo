package com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception;

/**
 * Created by zhulinping on 2017/9/22.
 */

/**
 * VCardException thrown when VCard is nested without VCardParser's being notified.
 */
public class VCardNestedException extends VCardNotSupportedException {
    public VCardNestedException() {
        super();
    }
    public VCardNestedException(String message) {
        super(message);
    }
}
