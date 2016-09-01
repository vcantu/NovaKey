package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 6/15/2016.
 */
public class UpdateSelectionAction implements Action<Void> {

    private boolean mMoveRight;

    public UpdateSelectionAction(boolean moveRight) {
        mMoveRight = moveRight;
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
        int ds = 0, de = 0;
        if (model.getCursorMode() <= 0)
            ds = mMoveRight ? 1 : -1;
        if (model.getCursorMode() >= 0)
            de = mMoveRight ? 1 : -1;
        control.fire(new SelectionActions.Move(ds, de));
        return null;
    }
}
