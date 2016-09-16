package viviano.cantu.novakey.controller.actions.typing;

import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetKeyboardAction;
import viviano.cantu.novakey.model.elements.keyboards.Keyboards;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.Settings;
import viviano.cantu.novakey.utils.Util;

/**
 * Universal input action
 * Can be given '\n', Keyboard.KEYCODE_SHIFT
 * or similar inputs and will perform the desired actions
 *
 * Created by Viviano on 6/14/2016.
 */
public class InputAction implements Action<Void> {

    private static boolean SHOULD_RETURN = false;//TODO: better system for should return

    //return after space
    private final Character[] returnAfterSpace = new Character[]
            { '.', ',', ';', '&', '!', '?' };
    public boolean shouldReturnAfterSpace(Character c) {
        for (Character C : returnAfterSpace) {
            if (C == c)
                return true;
        }
        return false;
    }
    //Closing Characters
    private int[] openers = new int[] { };
    private int[] closers = new int[] { };
    private int getIndex(int c) {
        for (int i=0; i<openers.length; i++) {
            if (openers[i] == c)
                return i;
        }
        return -1;
    }
    private boolean isOpener(int c) {
        for (int i : openers) {
            if (i == c)
                return true;
        }
        return false;
    }


    private final String mText;
    private final int mKeyCode;
    private final int mCursorPos;

    public InputAction(int keyCode) {
        this(keyCode, false);
    }

    public InputAction(int keyCode, boolean beforeCursor) {
        mCursorPos = beforeCursor ? 0 : 1;
        mKeyCode = keyCode;
        mText = null;
    }

    public InputAction(String text) {
        this(text, true);
    }

    public InputAction(String text, boolean beforeCursor) {
        mCursorPos = beforeCursor ? 0 : 1;
        mKeyCode = android.inputmethodservice.Keyboard.KEYCODE_CANCEL;
        mText = text;
    }

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, Model model) {
        InputConnection ic = ime.getCurrentInputConnection();
        if (ic == null)
            return null;

        if (mKeyCode != android.inputmethodservice.Keyboard.KEYCODE_CANCEL){
            if (mKeyCode != '\n' && mKeyCode >=0) {
                if (mKeyCode == ' ')
                    handleSpace(ime, control, model);
                else
                    handleChar(ime, control, model);
            }
            else {
                if (mKeyCode == '\n')
                    control.fire(new EnterAction());
                else if (mKeyCode == android.inputmethodservice.Keyboard.KEYCODE_SHIFT) {
                    control.fire(new ShiftAction());
                    return null;
                }
            }
        }
        else if (mText != null) {
            ic.finishComposingText();
            ime.composing.setLength(0);
            ic.commitText(mText, mCursorPos);
        }
        else if (mKey != null) {

        }
        control.fire(new UpdateShiftAction());
        return null;
    }

    private void handleChar(NovaKey ime, Controller control, Model model) {
        InputConnection ic = ime.getCurrentInputConnection();
        //it letter or number
        if (!model.getInputState().onPassword()
                && Settings.autoCorrect
                && (Character.isLetter(mKeyCode) || Util.isNumber(mKeyCode))) {
            if (ime.composingIndex >= ime.composing.length()) {
                ime.composing.append((char)mKeyCode);
                ic.setComposingText(ime.composing, mCursorPos);
                ime.composingIndex = ime.composing.length();
            }
            else {
                ime.commitComposing();
                ic.commitText(Character.toString((char)mKeyCode), mCursorPos);
            }
        }
        //if is opener
        else if (!model.getInputState().onPassword()
                && Settings.quickInsert
                && isOpener(mKeyCode)) {
            ime.commitComposing();
            int i = ic.getExtractedText(new ExtractedTextRequest(), 0).selectionStart + 1;
            ic.commitText(String.valueOf((char)mKeyCode) +
                    String.valueOf((char)closers[getIndex(mKeyCode)]), 1);
            ic.setSelection(i, i);
        }
        else {
            ime.commitComposing();
            ic.commitText(Character.toString((char)mKeyCode), mCursorPos);
        }

        SHOULD_RETURN = shouldReturnAfterSpace((char) mKeyCode); //set should return after space
        if (mKeyCode == '\'' || mKeyCode == '"')//upside down question mark
            control.fire(new SetKeyboardAction(Keyboards.DEFAULT));
    }

    private void handleSpace(NovaKey ime, Controller control, Model model) {
        InputConnection ic = ime.getCurrentInputConnection();
        ime.commitComposing();
        ic.commitText(" ", mCursorPos);
        if (SHOULD_RETURN)
            control.fire(new SetKeyboardAction(Keyboards.DEFAULT));
        SHOULD_RETURN = false;
    }



}
