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

package viviano.cantu.novakey.core.model;

import android.text.InputType;
import android.view.inputmethod.EditorInfo;

import java.util.Stack;

import viviano.cantu.novakey.core.utils.Util;

/**
 * Created by Viviano on 6/16/2016.
 * <p>
 * Represents the state of a typing session.
 * Various aspects of this state like Composing and cursor data
 * are synced with the input connection.
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
    // Current text of the session
    private StringBuilder mText = new StringBuilder();
    // Current text being composed
    private StringBuilder mComposing = new StringBuilder();
    private int mRepeatCount = 0;//current count of closing chars
    private boolean mReturnAfterSpace;
    // Deleting
    private Stack<String> mDeleteHistory = new Stack<>();
    // Positive if deleting forward, negative if backward.
    // Absolute value should always equal size of delete stack
    private int mDeleteProgress = 0;

    /**
     * Cursor data
     */
    private int mOldSelectionStart;
    private int mOldSelectionEnd;
    private int mSelectionStart;
    private int mSelectionEnd;
    private int mCandidatesStart;
    private int mCandidatesEnd;


    /**
     * Updates the input state's editor info so it can make the necessary
     * Changes
     * @param editorInfo info about the edit text
     */
    public void updateEditorInfo(EditorInfo editorInfo) {
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
     * Updates the input state of an updated selection
     */
    public void updateSelection(int oldSelStart, int oldSelEnd,
                                int newSelStart, int newSelEnd,
                                int candidatesStart, int candidatesEnd) {
        mOldSelectionStart = oldSelStart;
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
     * replaces the composing text
     *
     * @param text text to replace with
     */
    public void setComposingText(String text) {
        mComposing.setLength(0);
        mComposing.append(text);
    }


    /**
     * Adds text to the start of the composing text
     * @param text text to insert
     */
    public void insertComposingText(String text) {
        mComposing.insert(0, text);
    }


    /**
     * Adds text to end of the composing text
     * @param text text to append
     */
    public void appendComposingText(String text) {
        mComposing.append(text);
    }


    /**
     * Clears the composing text
     */
    public void clearComposingText() {
        mComposing.setLength(0);
    }


    /**
     * Deletes the start of the composing text by the given amount of characters
     * @param amount amount of characters
     * @return the deleted text
     */
    public String trimComposingStart(int amount) {
        int val =  Util.bounded(amount, 0, mComposing.length());
        String res = mComposing.substring(0, val);
        mComposing.delete(0, val);
        return res;
    }


    /**
     * Deletes the end of the composing text by the given amount of characters
     * @param amount amount of characters
     * @return the deleted text
     */
    public String trimComposingEnd(int amount) {
        int len = mComposing.length();
        int val =  Util.bounded(len - amount, 0, len);
        String res = mComposing.substring(val);
        mComposing.delete(val, len);
        return res;
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
    public void resetRepeatCount() {
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
    public int getOldSelectionStart() {
        return mOldSelectionStart;
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


    /**
     * @return if only one cursor or two in selection mode
     */
    public boolean oneCursor() {
        return mSelectionStart == mSelectionEnd;
    }


    /**
     * @return whether the cursor is currently within the composing text (not start or end)
     *  returns false if there is no composing text.
     */
    public boolean cursorWithinComposing() {
        return oneCursor() && mCandidatesStart != -1 && mCandidatesEnd != -1 &&
                mSelectionStart > mCandidatesStart && mSelectionStart < mCandidatesEnd;
    }


    /**
     * @return whether the cursor is currently at the start of the composing text
     *  returns false if there is no composing text.
     */
    public boolean cursorAtComposingStart() {
        return oneCursor() && mCandidatesStart != -1 && mCandidatesEnd != -1 &&
                mSelectionStart == mCandidatesStart;
    }


    /**
     * @return whether the cursor is currently at the end of the composing text
     *  returns false if there is no composing text.
     */
    public boolean cursorAtComposingEnd() {
        return oneCursor() && mCandidatesStart != -1 && mCandidatesEnd != -1 &&
                mSelectionStart == mCandidatesEnd;
    }


    /**
     * clears the delete history
     */
    public void resetDeleteHistory() {
        mDeleteHistory.clear();
        mDeleteProgress = 0;
    }


    /**
     * Notifies the state of a forwards delete action
     * @param text deleted text to save
     */
    public void deleteForwards(String text) {
        if (mDeleteProgress >= 0) {
            mDeleteHistory.push(text);
            mDeleteProgress++;
        }
    }


    /**
     * Notifies the state of a backwards delete action
     * @param text deleted text to save
     */
    public void deleteBackwards(String text) {
        if (mDeleteProgress <= 0) {
            mDeleteHistory.push(text);
            mDeleteProgress--;
        }
    }


    /**
     * Returns the last deleted text
     * @return text that was deleted
     */
    public String undoDelete() {
        if (mDeleteProgress > 0)
            mDeleteProgress--;
        else if (mDeleteProgress < 0)
            mDeleteProgress++;
        else
            throw new IllegalStateException("There is no delete to undo");
        return mDeleteHistory.pop();
    }


    /**
     * @return the progress of the current delete session.
     * Positive means forward deleting, negative is backwards deleting
     */
    public int getDeleteProgress() {
        return mDeleteProgress;
    }


}
