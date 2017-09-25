package com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception;

/**
 * Created by zhulinping on 2017/9/22.
 */

/**
 * Thrown when the vCard has some line starting with '#'. In the specification,
 * both vCard 2.1 and vCard 3.0 does not allow such line, but some actual exporter emit
 * such lines.
 */
public class VCardInvalidLineException extends VCardException {
    public VCardInvalidLineException() {
        super();
    }
    public VCardInvalidLineException(final String message) {
        super(message);
    }
}