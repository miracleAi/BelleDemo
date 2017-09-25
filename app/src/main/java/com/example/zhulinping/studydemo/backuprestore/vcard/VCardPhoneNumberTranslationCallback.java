package com.example.zhulinping.studydemo.backuprestore.vcard;

/**
 * Created by zhulinping on 2017/9/22.
 */
/**
 * Callback functionality which can be used with
 * {@link VCardComposer#setPhoneNumberTranslationCallback(VCardPhoneNumberTranslationCallback)}.
 * See the doc for the method.
 *
 * <p>
 * TODO: this should be more generic
 * </p>
 *
 * @hide This will change
 */
public interface VCardPhoneNumberTranslationCallback {
    /**
     * Called when a phone number is being handled.
     * @return formatted phone number.
     */
    public String onValueReceived(String rawValue, int type, String label, boolean isPrimary);
}
