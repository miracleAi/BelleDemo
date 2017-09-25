package com.example.zhulinping.studydemo.backuprestore.vcard;

/**
 * Created by zhulinping on 2017/9/22.
 */

import com.example.zhulinping.studydemo.backuprestore.vcard.vcardexception.VCardException;

import java.io.IOException;
import java.io.InputStream;

public abstract class VCardParser {
    /**
     * Registers one {@link VCardInterpreter} instance, which receives events along with
     * vCard parsing.
     *
     * @param interpreter
     */
    public abstract void addInterpreter(VCardInterpreter interpreter);
    /**
     * <p>Parses a whole InputStream as a vCard file and lets registered {@link VCardInterpreter}
     * instances handle callbacks.</p>
     *
     * <p>This method reads a whole InputStream. If you just want to parse one vCard entry inside
     * a vCard file with multiple entries, try {@link #parseOne(InputStream)}.</p>
     *
     * @param is The source to parse.
     * @throws IOException, VCardException
     */
    public abstract void parse(InputStream is) throws IOException, VCardException;
    /**
     * <p>Parses the first vCard entry in InputStream and lets registered {@link VCardInterpreter}
     * instances handle callbacks.</p>
     *
     * <p>This method finishes itself when the first entry ended.</p>
     *
     * <p>Note that, registered {@link VCardInterpreter} may still see multiple
     * {@link VCardInterpreter#onEntryStarted()} / {@link VCardInterpreter#onEntryEnded()} calls
     * even with this method.</p>
     *
     * <p>This happens when the first entry contains nested vCards, which is allowed in vCard 2.1.
     * See the following example.</p>
     *
     * <code>
     * BEGIN:VCARD
     * N:a
     * BEGIN:VCARD
     * N:b
     * END:VCARD
     * END:VCARD
     * </code>
     *
     * <p>With this vCard, registered interpreters will grab two
     * {@link VCardInterpreter#onEntryStarted()} and {@link VCardInterpreter#onEntryEnded()}
     * calls. Callers should handle the situation by themselves.</p>
     *
     * @param is  The source to parse.
     * @throws IOException, VCardException
     */
    public abstract void parseOne(InputStream is) throws IOException, VCardException;
    /**
     * @deprecated use {@link #addInterpreter(VCardInterpreter)} and
     * {@link #parse(InputStream)}
     */
    @Deprecated
    public void parse(InputStream is, VCardInterpreter interpreter)
            throws IOException, VCardException {
        addInterpreter(interpreter);
        parse(is);
    }
    /**
     * <p>
     * Cancel parsing vCard. Useful when you want to stop the parse in the other threads.
     * </p>
     * <p>
     * Actual cancel is done after parsing the current vcard.
     * </p>
     */
    public abstract void cancel();
}
