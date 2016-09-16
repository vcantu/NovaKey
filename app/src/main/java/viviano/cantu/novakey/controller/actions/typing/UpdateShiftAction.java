package viviano.cantu.novakey.controller.actions.typing;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.controller.actions.Action;
import viviano.cantu.novakey.controller.actions.SetShiftStateAction;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.states.ShiftState;

/**
 * Created by Viviano on 6/15/2016.
 */
public class UpdateShiftAction implements Action<Void> {
    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, Model model) {
        if (model.getKeyboardCode() >= 0) {
            switch (model.getShiftState()) {
                case LOWERCASE:
                case UPPERCASE:
                    if (ime.getCurrentCapsMode(
                            ime.getCurrentInputEditorInfo()) != 0)
                        control.fire(new SetShiftStateAction(ShiftState.UPPERCASE));
                    else
                        control.fire(new SetShiftStateAction(ShiftState.LOWERCASE));
                    break;
                case CAPS_LOCKED:
                    break;
            }
        }
        return null;
    }
}
