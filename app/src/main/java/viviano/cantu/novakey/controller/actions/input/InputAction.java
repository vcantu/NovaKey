package viviano.cantu.novakey.controller.actions.input;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.model.Model;
/**
 * Universal input action
 * Can be given '\n', Keyboard.KEYCODE_SHIFT
 * or similar inputs and will perform the desired actions
 *
 * Created by Viviano on 6/14/2016.
 */
public class InputAction implements Action<Void> {

    private final String mText;
    private final int mCursorPos;

    public InputAction(String text) {
        this(text, true);
    }

    public InputAction(String text, boolean beforeCursor) {
        mCursorPos = beforeCursor ? 0 : 1;
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
        if (mText != null) {
            model.getInputState().inputText(mText, mCursorPos);
        }
        return null;
    }
}
