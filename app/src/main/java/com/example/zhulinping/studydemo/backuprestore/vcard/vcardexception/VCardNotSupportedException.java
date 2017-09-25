package com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception;

/**
 * Created by zhulinping on 2017/9/22.
 */

/**
 * The exception which tells that the input VCard is probably valid from the view of
 * specification but not supported in the current framework for now.
 *
 * This is a kind of a good news from the view of development.
 * It may be good to ask users to send a report with the VCard example
 * for the future development.
 */
public class VCardNotSupportedException extends VCardException {
    public VCardNotSupportedException() {
        super();
    }
    public VCardNotSupportedException(String message) {
        super(message);
    }
}