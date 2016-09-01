package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.Model;

/**
 * Created by Viviano on 6/16/2016.
 */
public class ClipboardAction implements Action<String> {

    private final int mcbAction;

    public ClipboardAction(int cbAction) {
        mcbAction = cbAction;
    }
    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public String trigger(NovaKey ime, Controller control, Model model) {
        ime.handleClipboardAction(mcbAction);
        return null;
    }
}
