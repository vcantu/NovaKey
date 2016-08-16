package viviano.cantu.novakey.model.states;

import android.view.inputmethod.EditorInfo;

/**
 * Created by Viviano on 6/16/2016.
 */
public class InputState {

    public InputState(EditorInfo editorInfo) {
        //TODO: generate fields based on this
    }

    /**
     * @return whether the user is currently typing a password
     */
    public boolean onPassword() {
        return true;
    }

//    public static void onInputStart(EditorInfo info, boolean restarting) {
//
//        inputType = info.inputType;
//
//        model.setUserState(UserState.TYPING);
//        model.setShiftState(ShiftState.LOWERCASE);
//        int keyboardCode = 0;//TODO: add curr keyboard sharedPref
//        model.setKeyboard(keyboardCode);
//        onPassword = false;
//
//        int var = inputType & InputType.TYPE_MASK_VARIATION,
//                flags = inputType & InputType.TYPE_MASK_FLAGS;
//
//        switch (info.inputType & InputType.TYPE_MASK_CLASS) {
//            case InputType.TYPE_CLASS_TEXT:
//                model.setKeyboard(keyboardCode);
//                if (var == InputType.TYPE_TEXT_VARIATION_PASSWORD ||
//                        var == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD ||
//                        var == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD)
//                    onPassword = true;
//                break;
//            case InputType.TYPE_CLASS_NUMBER:
//                model.setKeyboard(KeyLayout.PUNCTUATION);
//                if (var == InputType.TYPE_NUMBER_VARIATION_PASSWORD)
//                    onPassword = true;
//                break;
//            case InputType.TYPE_CLASS_DATETIME:
//                model.setKeyboard(KeyLayout.PUNCTUATION);
//                break;
//            case InputType.TYPE_CLASS_PHONE:
//                model.setKeyboard(KeyLayout.PUNCTUATION);
//                break;
//        }
//    }
}
