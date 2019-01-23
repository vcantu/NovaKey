/*
 * NovaKey - An alternative touchscreen input method
 * Copyright (C) 2019  Viviano Cantu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 *
 * Any questions about the program or source may be directed to <strellastudios@gmail.com>
 */

package viviano.cantu.novakey.model;

import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import viviano.cantu.novakey.controller.Corrections;
import viviano.cantu.novakey.utils.Print;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 6/16/2016.
 *
 * Represents the state of a typing session
 */
public class InputState {

    private boolean mOnPassword = false;
    private boolean mOnEmailAddress = false;

    private boolean mOnURI = false;

    private Type mType = Type.TEXT;

    private int mRepeatCount = 0;//current count of closing chars
    private StringBuilder mComposing = new StringBuilder();
    private int mComposingIndex = 0;

    private InputConnection mConnection;

    private Corrections mCorrections;

    private boolean mReturnAfterSpace;

    public void setCorrections(Corrections corrections) {
        mCorrections = corrections;
    }

    public void updateConnection(EditorInfo editorInfo, InputConnection inputConnection) {
        mConnection = inputConnection;
        mComposing.setLength(0);

        int inputType = editorInfo.inputType;

        int var = inputType & InputType.TYPE_MASK_VARIATION,
                flags = inputType & InputType.TYPE_MASK_FLAGS;

        switch (inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_TEXT:
                mType = Type.TEXT;
                switch (inputType & InputType.TYPE_MASK_VARIATION) {
                    case InputType.TYPE_TEXT_VARIATION_PASSWORD:
                    case InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
                    case InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD:
                        mOnPassword = true;
                        break;
                    case InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                    case InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS:
                        mOnEmailAddress = true;
                        break;
                    case InputType.TYPE_TEXT_VARIATION_URI:
                        mOnURI = true;
                        break;
                }
                break;
            case InputType.TYPE_CLASS_NUMBER:
                mType = Type.NUMBER;
                if (var == InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                    mOnPassword = true;
                break;
            case InputType.TYPE_CLASS_DATETIME:
                mType = Type.DATETIME;
                break;
            case InputType.TYPE_CLASS_PHONE:
                mType = Type.PHONE;
                break;
        }
    }

    /**
     *
     * @param newSelStart
     * @param newSelEnd
     */
    public void onUpdateSelection(int newSelStart, int newSelEnd) {
        if (!Settings.autoCorrect && shouldAutoCorrect())
            return;
        //set composing region
        //if single cursor AND oldEnd is not newStart?
        if (newSelStart == newSelEnd) {
            ExtractedText et = getExtractedText();
            if (et == null) {
                mComposing.setLength(0);
                return;
            }
            String text = et.text.toString();
            if (text.length() == 0) {
                mComposing.setLength(0);
                return;
            }

            int e, s;//start of end of composing
            //loop from start of cursor to the left
            //if not a letter, number or ' adjust s and stop loop
            for (s = Math.min(text.length(), newSelStart); s > 0; s--) {
                char c = text.charAt(s - 1);
                if (!Character.isLetter(c) && !Util.isNumber(c) && c != '\'')
                    break;
            }

            //loop from end of cursor to the right
            //if not a letter, number or ' stop loop
            for (e = Math.min(text.length(), newSelEnd); e < text.length(); e++) {
                char c = text.charAt(e);
                if (!Character.isLetter(c) && !Util.isNumber(c) && c != '\'')
                    break;
            }

            Print.extText(et);

            //finally update ICs composing region
            //match composing region to our composing StringBuilder
            //update composing index
            mConnection.setComposingRegion(s, e);
            mComposing.setLength(0);
            mComposing.append(text.substring(s, e));
            mComposingIndex = newSelStart - s;
        }
        else {
            mConnection.finishComposingText();
            mComposing.setLength(0);
        }
    }

    /**
     *
     * @param newSelStart
     * @param newSelEnd
     */
    public void onUpdateSelectionOld(int newSelStart, int newSelEnd) {
        if (!Settings.autoCorrect && shouldAutoCorrect())
            return;
        //set composing region
        //if single cursor AND oldEnd is not newStart?
        if (newSelStart == newSelEnd) {
            ExtractedText et = getExtractedText();
            if (et == null) {
                mComposing.setLength(0);
                return;
            }
            String text = et.text.toString();
            if (text.length() == 0) {
                mComposing.setLength(0);
                return;
            }
            int e, s;//start of end of composing
            //loop from start of cursor to the left
            //if not a letter, number or ' adjust s and stop loop
            for (s = Math.min(text.length(), newSelStart); s > 0; s--) {
                char c = text.charAt(s - 1);
                if (!Character.isLetter(c) && !Util.isNumber(c) && c != '\'')
                    break;
            }

            //loop from end of cursor to the right
            //if not a letter, number or ' stop loop
            for (e = Math.min(text.length(), newSelEnd); e < text.length(); e++) {
                char c = text.charAt(e);
                if (!Character.isLetter(c) && !Util.isNumber(c) && c != '\'')
                    break;
            }

            Print.extText(et);

            //finally update ICs composing region
            //match composing region to our composing StringBuilder
            //update composing index
            mConnection.setComposingRegion(s, e);
            mComposing.setLength(0);
            mComposing.append(text.substring(s, e));
            mComposingIndex = newSelStart - s;
        }
        else {
            mConnection.finishComposingText();
            mComposing.setLength(0);
        }
    }

    /**
     * inputs the given text as is
     *
     * @param text         text to input
     * @param newCursorPos were the cursor should end
     */
    public void inputText(String text, int newCursorPos) {
        mConnection.finishComposingText();
        mComposing.setLength(0);

        mConnection.commitText(text, newCursorPos);
    }

    /**
     * releases the composing text and fixes it
     * if it should be fixed and it needs to be fixed
     */
    public void commitCorrection() {
        //AutoCorrect
        if (Settings.autoCorrect && !shouldAutoCorrect()) {
            setComposingText(mCorrections.correction(mComposing.toString()), 1);
            //TODO: multi language
            //TODO: caps locked
            //TODO: markov chains
        }
        commitComposing();
    }

    /**
     * releases the composing text
     */
    public void commitComposing() {
        mConnection.finishComposingText();
        mComposing.setLength(0);
    }

    /**
     * replaces the composing text
     *
     * @param text         text to replace with
     * @param newCursorPos where the cursor should end
     */
    public void setComposingText(String text, int newCursorPos) {
        mComposing.setLength(0);
        mComposing.append(text);
        mConnection.setComposingText(text, newCursorPos);
    }

    /**
     * move the selection from it's current position
     *
     * @param deltaStart difference of start cursor
     * @param deltaEnd   difference of end cursor
     */
    public void moveSelection(int deltaStart, int deltaEnd) {
        ExtractedText et = getExtractedText();
        int s = et.selectionStart + deltaStart, e = et.selectionEnd + deltaEnd;
        s = s < 0 ? 0 : s;
        e = e < 0 ? 0 : e;
        if (s <= e)
            mConnection.setSelection(s, e);
        else {
            mConnection.setSelection(e, s);
        }
    }

    /**
     * move the selection to the absolute position
     *
     * @param start start of cursor
     * @param end   end of cursor
     */
    public void setSelection(int start, int end) {
        mConnection.setSelection(start, start);
        mConnection.setSelection(start, end);
    }

    /**
     * @return extracted text object, which receives updates
     */
    public ExtractedText getExtractedText() {
        return mConnection.getExtractedText(new ExtractedTextRequest(), 0);
    }

    /**
     * delete text before & after cursor
     * @param beforeLength number of codepoints before the cursor to delete
     * @param afterLength number of codepoints after the cursor to delete
     */
    public void deleteSurroundingText(int beforeLength, int afterLength) {
        mConnection.deleteSurroundingText(beforeLength, afterLength);
    }

    /**
     * @return whether
     */
    public boolean returnAfterSpace() {
        return mReturnAfterSpace;
    }

    /**
     * @param returnAfterSpace true if should return back to default keys after space
     */
    public void setReturnAfterSpace(boolean returnAfterSpace) {
        mReturnAfterSpace = returnAfterSpace;
    }

    public String getSelectedText() {
        CharSequence cs = mConnection.getSelectedText(0);
        if (cs != null)
            return cs.toString();
        return "";
    }

    /**
     * Gets the current capitalization mode
     *
     * @param editorInfo uses editor info to get the caps mode
     * @return 1 for caps 0 for not caps
     */
    public int getCurrentCapsMode(EditorInfo editorInfo) {
        //if null caps if needed only
        if (mConnection == null)
            return (editorInfo.inputType & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT
                    && (editorInfo.inputType & 0x4000) == 0 ? 0 : 1;
        else
            return mConnection.getCursorCapsMode(editorInfo.inputType);
    }

    /**
     * adds 1 to the repeating character count
     */
    public void incrementRepeat() {
        mRepeatCount++;
    }

    /**
     * sets repeating character count to 0
     */
    public void resetRepeat() {
        mRepeatCount = 0;
    }

    /**
     * @return the current repeating characters inputed
     */
    public int getRepeatCount() {
        return mRepeatCount;
    }

    /**
     * @return whether the user is currently typing a password
     */
    public boolean onPassword() {
        return mOnPassword;
    }

    /**
     * @return whether the user is currently typing on an email address field
     */
    public boolean onEmailAddress() { return mOnEmailAddress; }

    /**
     * @return whether the user is currently typing on a URI
     */
    public boolean onURI() {
        return mOnURI;
    }

    /**
     * @return whether it should use autocorrect and composing text
     */
    public boolean shouldAutoCorrect() {
        return onPassword() && onEmailAddress() && onURI();
    }

    /**
     * @return the current user's type class
     */
    public Type getType() {
        return mType;
    }

    public enum Type {
        TEXT,
        NUMBER,
        PHONE,
        DATETIME
    }
}
