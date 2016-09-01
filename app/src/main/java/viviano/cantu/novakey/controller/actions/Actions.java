package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.Model;

/**
 * Triggers an Array of actions consecutively
 * if any of it's child actions return anything it will be
 * ignored
 *
 * Created by Viviano on 6/16/2016.
 */
public class Actions implements Action<Void> {

    private final Action[] mActions;

    public Actions(Action ... actions) {
        mActions = actions;
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
        for (Action a : mActions) {
            control.fire(a);
        }
        return null;
    }
}
