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

package viviano.cantu.novakey;

import android.content.ClipboardManager;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.Vibrator;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;
import java.util.List;

import viviano.cantu.novakey.core.Clipboard;
import viviano.cantu.novakey.core.actions.input.UpdateShiftAction;
import viviano.cantu.novakey.core.controller.Controller;
import viviano.cantu.novakey.core.controller.Corrections;
import viviano.cantu.novakey.core.NovaKeyService;
import viviano.cantu.novakey.core.model.InputState;
import viviano.cantu.novakey.core.model.Settings;
import viviano.cantu.novakey.core.utils.Predicate;
import viviano.cantu.novakey.core.utils.Print;
import viviano.cantu.novakey.core.utils.Util;
import viviano.cantu.novakey.core.view.themes.AppTheme;

public class MainNovaKeyService extends NovaKeyService {

    //Statics
    public static String MY_PREFERENCES = "MyPreferences";
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    //Services
    private Vibrator vibrator;
    private ClipboardManager clipboard;
    private WindowManager windowManager;
    private List<View> mWindows;

    private Controller mController;

    private final Predicate<Character> mComposingChar =
            c -> Character.isLetter(c) || Util.isNumber(c) || c == '\'';
    private final Predicate<String> mComposingString = s -> {
        for (int i = 0; i < s.length(); i++) {
            if (!mComposingChar.test(s.charAt(i)))
                return false;
        }
        return true;
    };

    /*
     Items initialized on create:
      X : non-necessary
      ? : conditionally necessary
      S : slow

        External:
        - ? Vibrator
        - ? Window Manager
        -   Clipboard

        Initializers:
        -    Colors
        - ?S AutoColor (AppTheme) if enabled
        - ?S Fonts
        - ?S Emoji
        -    Infinite Keys Menu
        -    Clipboard Menu
        - ?S Corrections
        -    Settings (sync with shared preferences)
                - Elements
                - Dimensions
                - Themes
                - Keyboards
                    - Keys
     */


    // ---------------- Start of life cycle ----------------

    /**
     * Called when the keyboard is enabled
     */
    @Override
    public void onCreate() {
        getApplicationContext().setTheme(R.style.AppTheme);
        super.onCreate();

        //Phone Services
        clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(() -> {
            try {
                Clipboard.add(clipboard.getPrimaryClip().getItemAt(0).getText().toString());
            } catch (NullPointerException ignored) { }
        });
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindows = new ArrayList<>();

        //Controller creation
        mController = new Controller(this);

        //Shared Preferences for setup activity //TODO: change this
        Editor temp = getApplicationContext().getSharedPreferences(MainNovaKeyService.MY_PREFERENCES, MODE_PRIVATE).edit();
        temp.putBoolean("has_setup", true);
        temp.commit();

    }


    @Override
    public View onCreateInputView() {
        AppTheme.load(this, getResources());

        View v = mController.getView();
        //must review parent
        if (v.getParent() != null)
            ((ViewGroup) v.getParent()).removeView(v);
        return v;
    }


    /**
     * Called when the connection is bound.
     * This makes getCurrentInputConnection() and getCurrentInputBinding() valid
     */
    @Override
    public void onBindInput() {
    }


    /**
     * Called whenever the user begins to type in a field
     * This implementation takes care of updating the model and invalidating
     */
    @Override
    public void onStartInput(EditorInfo info, boolean restarting) {
        super.onStartInput(info, restarting);

        mController.getModel().onStart(info);
        mController.invalidate();
    }


    //onShowInputRequested
    //onStartInputView


    /**
     * Called when the IME updates the selection
     */
    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd,
                                  int newSelStart, int newSelEnd,
                                  int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(
                oldSelStart, oldSelEnd,
                newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);

        InputState state = mController.getModel().getInputState();
        // Update input state
        state.updateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);

        if (!Settings.autoCorrect || !state.shouldAutoCorrect()) {
            // clear composing text just in case
            state.clearComposingText();
            return;
        }

        ExtractedText et = getExtractedText();
        // if not a single cursor get rid of composing text
        if (newSelStart != newSelEnd || et == null || et.text.length() == 0) {
            commitComposingText();
        }
        //if single cursor and text > 1 char
        else {
            String text = et.text.toString();
            int e, s;//start of end of composing

            //loop from start of cursor to the left
            //if not a letter, number or ' adjust s and stop loop
            for (s = Math.min(text.length(), newSelStart); s > 0; s--) {
                char c = text.charAt(s - 1);
                if (!mComposingChar.test(c))
                    break;
            }

            //loop from end of cursor to the right
            //if not a letter, number or ' stop loop
            for (e = Math.min(text.length(), newSelEnd); e < text.length(); e++) {
                char c = text.charAt(e);
                if (!mComposingChar.test(c))
                    break;
            }

            // finally update ICs composing region if needed
            if (candidatesStart != s || candidatesEnd != e) {
                // match composing region to our composing StringBuilder
                // update composing index
                getCurrentInputConnection().setComposingRegion(s, e);
                state.setComposingText(text.substring(s, e));
            }
        }
    }


    @Override
    public void onExtractingInputChanged(EditorInfo ei) {
    }


    @Override
    public void onUpdateExtractedText(int token, ExtractedText text) {
    }


    // Pre API 21 -> onUpdateCursor(Rect newCursor)
    @Override
    public void onUpdateCursorAnchorInfo(CursorAnchorInfo cursorAnchorInfo) {
    }


    /**
     * Fires the ResetStateAction
     * called when the IME disconnects from the Editor
     */
    @Override
    public void onFinishInput() {
        super.onFinishInput();
        //TODO: reset state
        mController.invalidate();
    }


    /**
     * Called when the IME is destroyed.
     * This is where memory can be released.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    // ---------------- End of life cycle ----------------


    /**
     * Makes it never go on Fullscreen Mode
     *
     * @return false always
     */
    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }


    /**
     * @return this device's clipboard manager
     */
    public ClipboardManager getClipboard() {
        return clipboard;
    }


    /**
     * Vibrates the device given an amount of milliseconds
     *
     * @param milliseconds
     */
    public void vibrate(long milliseconds) {
        if (Settings.vibrate)
            vibrator.vibrate(milliseconds);
    }


    /**
     * inputs the given text as is
     *
     * @param text         text to input
     * @param newCursorPos were the cursor should end
     */
    public void inputText(String text, int newCursorPos) {
        InputState state = mController.getModel().getInputState();

        // if we can just append or insert to the composing text then do that
        if (Settings.autoCorrect && state.shouldAutoCorrect() && mComposingString.test(text)) {
            // if at end of composing
            if (newCursorPos == 1 && state.cursorAtComposingEnd()) {
                state.appendComposingText(text);
                getCurrentInputConnection().setComposingText(state.getComposingText(), newCursorPos);
                return;
            }
            // if at start of composing
            else if (newCursorPos == 0 && state.cursorAtComposingStart()) {
                state.insertComposingText(text);
                getCurrentInputConnection().setComposingText(state.getComposingText(), newCursorPos);
                return;
            }

        }

        commitComposingText();
        getCurrentInputConnection().commitText(text, newCursorPos);
    }

    /**
     * Deletes the characters until a given predicate in the given direction
     *
     * @param until determines if the character should be deleted
     * @param backwards direction of the delete (true acts like a backspace, false like a forward delete)
     * @return the text that was deleted
     */
    @Override
    public String deleteText(Predicate<Character> until, boolean backwards) {
        InputConnection ic = getCurrentInputConnection();
        InputState is = mController.getModel().getInputState();
        if (ic == null)
            return null;

        StringBuilder sb = new StringBuilder();
        ExtractedText et = getExtractedText();
        String text = (String) et.text;
        String left = text.substring(0, et.selectionStart);
        String right = text.substring(et.selectionEnd);

        // set the first character
        char currChar = 0;
        if (backwards) {
            if (left.length() > 0)
                currChar = left.charAt(left.length() - 1);
        } else {
            if (right.length() > 0)
                currChar = right.charAt(0);
        }

        // start loop
        int i = 1;
        while (!until.test(currChar) && currChar != 0) {
            if (backwards)
                sb.insert(0, currChar);
            else
                sb.append(currChar);

            currChar = 0;
            if (backwards) {
                if (left.length() - i > 0)
                    currChar = left.charAt(left.length() - 1 - i);
            } else {
                if (right.length() - i > 0)
                    currChar = right.charAt(i);
            }
            i++;
        }
        // include the last character
        if (currChar != 0) {
            if (backwards)
                sb.insert(0, currChar);
            else
                sb.append(currChar);
        }


        System.out.println("finish composing");
        ic.finishComposingText();
        is.clearComposingText();
        if (sb.length() >= 1) {
            System.out.println("deleting surrounding");
            if (backwards)
                ic.deleteSurroundingText(sb.length(), 0);
            else
                ic.deleteSurroundingText(0, sb.length());
        }

        // add to delete stack in input text
        if (backwards)
            is.deleteBackwards(sb.toString());
        else
            is.deleteForwards(sb.toString());

        return sb.toString();
    }


    /**
     * This method is expensive, avoid it
     *
     * @return extracted text object, which receives updates
     */
    public ExtractedText getExtractedText() {
        return getCurrentInputConnection().getExtractedText(new ExtractedTextRequest(), 0);
    }


    /**
     * Get text that's selected
     *
     * @return text between the selection start and end
     */
    public String getSelectedText() {
        CharSequence cs = getCurrentInputConnection().getSelectedText(0);
        if (cs != null)
            return cs.toString();
        return "";
    }


    /**
     * Gets the current capitalization mode at the current cursor position
     *
     * @return 1 for caps 0 for not caps
     */
    public int getCurrentCapsMode() {
        EditorInfo ei = getCurrentInputEditorInfo();
        InputConnection ic = getCurrentInputConnection();
        //if null caps if needed only
        if (ic == null)
            return (ei.inputType & InputType.TYPE_MASK_CLASS) == InputType.TYPE_CLASS_TEXT
                    && (ei.inputType & 0x4000) == 0 ? 0 : 1;
        else
            return ic.getCursorCapsMode(ei.inputType);
    }


    /**
     * move the selection from it's current position
     *
     * @param deltaStart difference of start cursor
     * @param deltaEnd   difference of end cursor
     */
    public void moveSelection(int deltaStart, int deltaEnd) {
        InputState state = mController.getModel().getInputState();
        int s = Math.max(state.getSelectionStart() + deltaStart, 0),
            e = Math.max(state.getSelectionEnd() + deltaEnd, 0);
        if (s <= e)
            getCurrentInputConnection().setSelection(s, e);
        else {
            getCurrentInputConnection().setSelection(e, s);
        }
    }


    /**
     * move the selection to the absolute position
     *
     * @param start start of cursor
     * @param end   end of cursor
     */
    public void setSelection(int start, int end) {
        // need to set both starts then start and end in order for it to work
        getCurrentInputConnection().setSelection(start, start);
        getCurrentInputConnection().setSelection(start, end);
    }


    /**
     * Commits the current composing text with the best possible correction
     */
    public void commitCorrection() {
        InputConnection ic = getCurrentInputConnection();
        InputState is = mController.getModel().getInputState();
        Corrections corrections = mController.getModel().getCorrections();

        String text = corrections.correction(is.getComposingText());
        // not calling commitReplacementText(text) in case the logic changes later

        // update is can ic
        is.setComposingText(text);
        ic.setComposingText(text, 1);

        // clear is and ic
        // not calling this.commitComposingText() in case logic changes
        ic.finishComposingText();
        is.clearComposingText();
    }


    /**
     * Commits the given text replacing the current composing text
     *
     * @param text text to commit
     */
    public void commitReplacementText(String text) {
        InputConnection ic = getCurrentInputConnection();
        InputState is = mController.getModel().getInputState();

        // update is can ic
        is.setComposingText(text);
        ic.setComposingText(text, 1);

        // clear is and ic
        // not calling this.commitComposingText() in case logic changes
        ic.finishComposingText();
        is.clearComposingText();
    }


    /**
     * Commits the current composing text to the editor without corrections
     */
    public void commitComposingText() {
        getCurrentInputConnection().finishComposingText();
        mController.getModel().getInputState().clearComposingText();
    }


    // ---------------- Start of floating view code ----------------


    public void addWindow(View view, boolean fullscreen) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                fullscreen ? WindowManager.LayoutParams.MATCH_PARENT :
                        WindowManager.LayoutParams.WRAP_CONTENT,
                fullscreen ? WindowManager.LayoutParams.MATCH_PARENT :
                        WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 0;

        mWindows.add(view);
        windowManager.addView(view, params);
    }


    public void clearWindows() {
        for (int i = 0; i < mWindows.size(); i++) {
            windowManager.removeView(mWindows.remove(i));
        }
    }


    /**
     * When any of the back/home/appswitch buttons are clicked
     * TODO: more sophisticated floating view lifecycle
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (false) { // should be if undocked
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH)
                close();
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * Creates a new floating view
     */
    private void open() {
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        windowManager.addView(mController.getView(), params);
    }


    /**
     * Closes the floating view
     */
    private void close() {
        try {
            windowManager.removeView(mController.getView());
        } catch (Exception e) {
        }
    }

    // ---------------- End of floating view code ----------------

}