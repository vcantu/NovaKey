package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.elements.keyboards.overlays.OverlayElement;

/**
 * Created by Viviano on 6/16/2016.
 */
public class SetOverlayAction implements Action<Void> {

    private final OverlayElement mElement;

    public SetOverlayAction(OverlayElement element) {
        mElement = element;
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
        //TODO: animation
        //control.changeHandler(null);
        model.setOverlayElement(mElement);
        return null;
    }
}
