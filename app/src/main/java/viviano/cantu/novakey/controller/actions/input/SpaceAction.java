package viviano.cantu.novakey.controller.actions.input;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetKeyboardAction;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.elements.keyboards.Keyboards;
import viviano.cantu.novakey.model.InputState;

/**
 * Created by vcantu on 9/18/16.
 */
public class SpaceAction implements Action<Void> {

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
        InputState state = model.getInputState();

        state.inputText(" ", 1);
        if (state.returnAfterSpace())
            control.fire(new SetKeyboardAction(Keyboards.DEFAULT));
        state.setReturnAfterSpace(false);
        control.fire(new UpdateShiftAction());
        return null;
    }
}
