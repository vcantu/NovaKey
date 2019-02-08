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

/**
 * Created by Viviano on 6/16/2016.
 * <p>
 * Represents the state of a typing session
 */
public class InputState {

    /**
     * Editor data
     */
    private boolean mOnPassword = false;
    private boolean mOnEmailAddress = false;
    private boolean mOnURI = false;
    private Type mType = Type.TEXT;

    /**
     * Session data
     */
    private int mRepeatCount = 0;//current count of closing chars
    private StringBuilder mComposing = new StringBuilder();
    private boolean mReturnAfterSpace;

    /**
     * Cursor data
     */
    private int mOldSelelectionStart;
    private int mOldSelectionEnd;
    private int mSelectionStart;
    private int mSelectionEnd;
    private int mCandidatesStart;
    private int mCandidatesEnd;


    public void updateEditorInfo(EditorInfo editorInfo) {
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
     * The goal of this method in this class is to update the input state
     */
    public void updateSelection(int oldSelStart, int oldSelEnd,
                                int newSelStart, int newSelEnd,
                                int candidatesStart, int candidatesEnd) {
        /**
         * Update cursors
         */
        mOldSelelectionStart = oldSelStart;
        mOldSelectionEnd = oldSelEnd;
        mSelectionStart = newSelStart;
        mSelectionEnd = newSelEnd;
        mCandidatesStart = candidatesStart;
        mCandidatesEnd = candidatesEnd;
    }


    /**
     * @return current composing string
     */
    public String getComposingText() {
        return mComposing.toString();
    }


    /**
     * Clears the composing text
     */
    public void clearComposingText() {
        mComposing.setLength(0);
    }


    /**
     * replaces the composing text
     *
     * @param text text to replace with
     */
    public void setComposingText(String text) {
        mComposing.setLength(0);
        mComposing.append(text);
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
    public boolean onEmailAddress() {
        return mOnEmailAddress;
    }


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
        return !onPassword() && !onEmailAddress() && !onURI();
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


    /**
     * @return previous start of selection
     */
    public int getOldSelelectionStart() {
        return mOldSelelectionStart;
    }


    /**
     * @return previous end of selection
     */
    public int getOldSelectionEnd() {
        return mOldSelectionEnd;
    }


    /**
     * @return current start of selection
     */
    public int getSelectionStart() {
        return mSelectionStart;
    }


    /**
     * @return curret end of selection
     */
    public int getSelectionEnd() {
        return mSelectionEnd;
    }


    /**
     * @return start of composing text or -1 if no composing
     */
    public int getCandidatesStart() {
        return mCandidatesStart;
    }


    /**
     * @return end of composing text or -1 if no composing
     */
    public int getCandidatesEnd() {
        return mCandidatesEnd;
    }
}
