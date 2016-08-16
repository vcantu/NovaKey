package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.NovaKeyModel;

/**
 * Toggles the cursor mode state accordingly
 *
 * Created by Viviano on 6/15/2016.
 */
public class ToggleCursorModeAction implements Action<Void> {


    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        int res = 0;
        switch (model.getCursorMode()) {
            case 0:
                res = -1;
                break;
            case -1:
                res = 1;
        }
        //TODO: Animations
        model.setCursorMode(res);
        return null;
    }
}
