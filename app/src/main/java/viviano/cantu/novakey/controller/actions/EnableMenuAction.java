package viviano.cantu.novakey.controller.actions;

import viviano.cantu.novakey.NovaKey;
import viviano.cantu.novakey.controller.Controller;
import viviano.cantu.novakey.elements.menus.Menu;
import viviano.cantu.novakey.model.NovaKeyModel;
import viviano.cantu.novakey.model.states.UserState;

/**
 * Created by Viviano on 6/16/2016.
 */
public class EnableMenuAction implements Action<Void> {

    private final Menu mMenu;

    public EnableMenuAction(Menu menu) {
        mMenu = menu;
    }

    /**
     * Called when the action is triggered
     * Actual logic for the action goes here
     * @param ime
     * @param control
     * @param model
     */
    @Override
    public Void trigger(NovaKey ime, Controller control, NovaKeyModel model) {
        //TODO: animations
        model.setMenu(mMenu);
        model.setUserState(UserState.ON_MENU);
        control.getManager().setHandler(mMenu.getTouchHandler(control));
        return null;
    }
}
