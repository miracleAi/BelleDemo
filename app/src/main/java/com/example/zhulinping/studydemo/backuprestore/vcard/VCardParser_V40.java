package com.example.zhulinping.studydemo.backuprestore.vcard;

/**
 * Created by zhulinping on 2017/9/22.
 */

import com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception.VCardException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * vCard parser for vCard 4.0. DO NOT USE IN PRODUCTION.
 * </p>
 * <p>
 * Currently this parser is based on vCard 4.0 specification rev 15 (partly).
 * Note that some of current implementation lack basic capability required in vCard 4.0.
 * (e.g. PHOTO has data parameter in rev 15 while this implementation requires "ENCODING=b")
 * </p>
 */
public class VCardParser_V40 extends VCardParser {
    /* package */ static final Set<String> sKnownPropertyNameSet =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
                    "BEGIN", "END", "VERSION",
                    "SOURCE", "KIND", "FN", "N", "NICKNAME",
                    "PHOTO", "BDAY", "ANNIVERSARY", "GENDER", "ADR", "TEL",
                    "EMAIL", "IMPP", "LANG", "TZ", "GEO", "TITLE", "ROLE",
                    "LOGO", "ORG", "MEMBER", "RELATED", "CATEGORIES",
                    "NOTE", "PRODID", "REV", "SOUND", "UID", "CLIENTPIDMAP",
                    "URL", "KEY", "FBURL", "CALENDRURI", "CALURI", "XML")));
    /**
     * <p>
     * A unmodifiable Set storing the values for the type "ENCODING", available in vCard 4.0.
     * </p>
     */
    /* package */ static final Set<String> sAcceptableEncoding =
            Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
                    VCardConstants.PARAM_ENCODING_8BIT,
                    VCardConstants.PARAM_ENCODING_B)));
    private final VCardParserImpl_V40 mVCardParserImpl;
    public VCardParser_V40() {
        mVCardParserImpl = new VCardParserImpl_V40();
    }
    public VCardParser_V40(int vcardType) {
        mVCardParserImpl = new VCardParserImpl_V40(vcardType);
    }
    @Override
    public void addInterpreter(VCardInterpreter interpreter) {
        mVCardParserImpl.addInterpreter(interpreter);
    }
    @Override
    public void parse(InputStream is) throws IOException, VCardException {
        mVCardParserImpl.parse(is);
    }
    @Override
    public void parseOne(InputStream is) throws IOException, VCardException {
        mVCardParserImpl.parseOne(is);
    }
    @Override
    public void cancel() {
        mVCardParserImpl.cancel();
    }
}
