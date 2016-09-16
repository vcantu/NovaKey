package viviano.cantu.novakey.model.states;

import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

/**
 * Created by Viviano on 6/16/2016.
 */
public class InputState {

    private boolean mOnPassword = false;
    private boolean mOnEmailAddress = false;

    private boolean mOnURI = false;

    private Type mType = Type.TEXT;

    private InputConnection mConnection;

    private int mCloserCount = 0;


    public void updateConnection(EditorInfo editorInfo, InputConnection inputConnection) {
        mConnection = inputConnection;
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

    public enum Type {
        TEXT,
        NUMBER,
        PHONE,
        DATETIME
    }
}
