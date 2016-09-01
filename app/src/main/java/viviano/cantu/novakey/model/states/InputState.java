package viviano.cantu.novakey.model.states;

import android.text.InputType;
import android.view.inputmethod.EditorInfo;

/**
 * Created by Viviano on 6/16/2016.
 */
public class InputState {

    private boolean mOnPassword = false;
    private boolean mOnEmailAddress = false;

    private boolean mOnURI = false;

    private Type mType = Type.TEXT;

    public InputState(EditorInfo editorInfo) {
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
     * @return the current user's type class
     */
    public Type getType() {
        return mType;
    }

//    public static void onInputStart(EditorInfo info, boolean restarting) {
//
//    int inputType = editorInfo.inputType;
//
//    model.setUserState(UserState.TYPING);
//    model.setShiftState(ShiftState.LOWERCASE);
//    int keyboardCode = 0;//TODO: add curr keyboard sharedPref
//    model.setKeyboard(keyboardCode);
//    mOnPassword = false;
//
//    int var = inputType & InputType.TYPE_MASK_VARIATION,
//            flags = inputType & InputType.TYPE_MASK_FLAGS;
//
//    switch (info.inputType & InputType.TYPE_MASK_CLASS) {
//        case InputType.TYPE_CLASS_TEXT:
//            model.setKeyboard(keyboardCode);
//            if (var == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
//                    var == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
//                    var == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD)
//                mOnPassword = true;
//            break;
//        case InputType.TYPE_CLASS_NUMBER:
//            model.setKeyboard(Keyboard.PUNCTUATION);
//            if (var == InputType.TYPE_NUMBER_VARIATION_PASSWORD)
//                mOnPassword = true;
//            break;
//        case InputType.TYPE_CLASS_DATETIME:
//            model.setKeyboard(Keyboard.PUNCTUATION);
//            break;
//        case InputType.TYPE_CLASS_PHONE:
//            model.setKeyboard(Keyboard.PUNCTUATION);
//            break;
//    }

    public enum Type {
        TEXT,
        NUMBER,
        PHONE,
        DATETIME
    }
}
