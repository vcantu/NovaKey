package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.elements.Element;
import viviano.cantu.novakey.elements.menus.Menu;
import viviano.cantu.novakey.model.Model;
import viviano.cantu.novakey.model.states.UserState;

/**
 * Created by Viviano on 6/16/2016.
 */
public class SetOverlayAction implements Action<Void> {

    private final Element mElement;

    public SetOverlayAction(Element element) {
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
        model.setOverlayElement(mElement);
        model.setUserState(UserState.ON_MENU);
        control.getManager().setHandler(mElement.getTouchHandler());
        return null;
    }
}
