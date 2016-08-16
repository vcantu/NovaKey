package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.NovaKeyModel;

/**
 * Does nothing, used as a filler for classes that require
 * non-null actions but no action is wanted
 *
 * Created by Viviano on 6/16/2016.
 */
public class NoAction implements Action<Void> {
    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        return null;
    }
}
