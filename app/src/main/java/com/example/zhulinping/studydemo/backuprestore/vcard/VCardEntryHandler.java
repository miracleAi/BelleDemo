package com.example.zhulinping.studydemo.backuprestore.vcard;

/**
 * Created by zhulinping on 2017/9/22.
 */

public interface VCardEntryHandler {
    /**
     * Called when the parsing started.
     */
    public void onStart();
    /**
     * The method called when one vCard entry is created. Children come before their parent in
     * nested vCard files.
     *
     * e.g.
     * In the following vCard, the entry for "entry2" comes before one for "entry1".
     * <code>
     * BEGIN:VCARD
     * N:entry1
     * BEGIN:VCARD
     * N:entry2
     * END:VCARD
     * END:VCARD
     * </code>
     */
    public void onEntryCreated(final VCardEntry entry);
    /**
     * Called when the parsing ended.
     * Able to be use this method for showing performance log, etc.
     */
    public void onEnd();
}
