package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.NovaKeyModel;

/**
 * Created by Viviano on 6/15/2016.
 */
public class SetKeyboardAction implements Action<Void> {

    private final int mKeyboardCode;

    public SetKeyboardAction(int keyboardCode) {
        mKeyboardCode = keyboardCode;
    }

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        //TODO: Animations
        model.setKeyboard(mKeyboardCode);
        return null;
    }
}
