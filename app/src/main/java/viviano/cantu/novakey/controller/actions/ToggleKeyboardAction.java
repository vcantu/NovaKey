package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.NovaKeyModel;
import viviano.cantu.novakey.model.keyboards.KeyLayout;

/**
 * Created by Viviano on 6/21/2016.
 */
public class ToggleKeyboardAction implements Action<Void> {

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     *
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        switch (model.getKeyboardCode()) {
            case KeyLayout.DEFAULT:
                control.fire(new SetKeyboardAction(KeyLayout.PUNCTUATION));
                break;
            default:
            case KeyLayout.PUNCTUATION:
            case KeyLayout.SYMBOLS:
                control.fire(new SetKeyboardAction(KeyLayout.DEFAULT));
        }
        return null;
    }
}
