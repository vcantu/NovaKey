package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.ShiftState;

/**
 * Created by Viviano on 6/15/2016.
 */
public class SetShiftStateAction implements Action<Void> {

    private final ShiftState mSetTo;

    public SetShiftStateAction(ShiftState setTo) {
        this.mSetTo = setTo;
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
        //TODO: ANIMATIONS
        model.setShiftState(mSetTo);
        return null;
    }
}
