package com.example.zhulinping.studydemo.backuprestore.vcard;

/**
 * Created by zhulinping on 2017/9/22.
 */

/**
 * The class which just counts the number of vCard entries in the specified input.
 */
public class VCardEntryCounter implements VCardInterpreter {
    private int mCount;
    public int getCount() {
        return mCount;
    }
    @Override
    public void onVCardStarted() {
    }
    @Override
    public void onVCardEnded() {
    }
    @Override
    public void onEntryStarted() {
    }
    @Override
    public void onEntryEnded() {
        mCount++;
    }
    @Override
    public void onPropertyCreated(VCardProperty property) {
    }
}