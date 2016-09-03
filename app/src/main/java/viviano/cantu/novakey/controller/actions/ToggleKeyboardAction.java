package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.elements.keyboards.Keyboards;
import viviano.cantu.novakey.model.Model;

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
    public Void trigger(NovaKey ime, Controller control, Model model) {
        switch (model.getKeyboardCode()) {
            case Keyboards.DEFAULT:
                control.fire(new SetKeyboardAction(Keyboards.PUNCTUATION));
                break;
            default:
            case Keyboards.PUNCTUATION:
            case Keyboards.SYMBOLS:
                control.fire(new SetKeyboardAction(Keyboards.DEFAULT));
        }
        return null;
    }
}
